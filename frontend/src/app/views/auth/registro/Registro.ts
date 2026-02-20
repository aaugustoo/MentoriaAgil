import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../auth/auth.service';
import { User } from '../../../models/User';

@Component({
  selector: 'app-cadastro',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './registro.html',
})
export class Registro implements OnInit {
  cadastroForm!: FormGroup;
  isLoading = false;

  // Controla visualmente qual card está selecionado no HTML
  selectedRole: 'ESTUDANTE' | 'MENTOR' = 'ESTUDANTE';

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authService: AuthService,
  ) {}

  ngOnInit(): void {
    this.cadastroForm = this.fb.group({
      nome: ['', [Validators.required]],
      email: [
        '',
        [
          Validators.required,
          Validators.email,
          Validators.pattern(/^[a-z0-9._%+-]+@ufape\.edu\.br$/),
        ],
      ],
      senha: ['', [Validators.required, Validators.minLength(8)]],
      // Inicia com o valor padrão selecionado
      role: ['ESTUDANTE', [Validators.required]],
      termos: [false, [Validators.requiredTrue]],
    });
  }

  /**
   * Atualiza a seleção visual e o valor no formulário reativo
   */
  selectRole(role: 'ESTUDANTE' | 'MENTOR') {
    this.selectedRole = role;
    this.cadastroForm.patchValue({ role: role });
  }

  irLogin() {
    this.router.navigate(['/login']);
  }
  irTermos() {
    this.router.navigate(['/termos']);
  }

  onSubmit() {
    if (this.cadastroForm.invalid) return;

    this.isLoading = true;
    const { nome, email, senha, role } = this.cadastroForm.value;

    const novoUsuario: User = { name: nome, email, password: senha, role };

    // 1. Primeiro realiza o Cadastro
    this.authService.register(novoUsuario).subscribe({
      next: () => {
        // 2. Se o cadastro deu certo, realiza o Login automático
        this.authService.login(email, senha).subscribe({
          next: (sucesso) => {
            this.isLoading = false;
            if (sucesso) {
              // 3. Agora autenticado, a navegação para rotas protegidas funcionará
              if (role === 'MENTOR') {
                this.router.navigate(['/mentor/cadastro']);
              } else {
                this.router.navigate(['/dashboard']);
              }
            }
          },
          error: () => {
            this.isLoading = false;
            this.router.navigate(['/login']); // Fallback caso o auto-login falhe
          },
        });
      },
      error: (err) => {
        this.isLoading = false;
        alert(err.error?.message || 'Erro no cadastro');
      },
    });
  }
}
