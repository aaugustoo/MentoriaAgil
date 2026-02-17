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

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authService: AuthService
  ) {}

  irLogin() {
    this.router.navigate(['/login']);
  }

  irTermos() {
    this.router.navigate(['/termos']);
  }

  ngOnInit(): void {
    this.cadastroForm = this.fb.group({
      nome: ['', [Validators.required]],
      // Validação E-mail UFAPE
      email: [
        '',
        [
          Validators.required,
          Validators.email,
          Validators.pattern(/^[a-z0-9._%+-]+@ufape\.edu\.br$/),
        ],
      ],
      // Validação Senha 8 caracteres
      senha: ['', [Validators.required, Validators.minLength(8)]],
      role: ['', [Validators.required]],
      termos: [false, [Validators.requiredTrue]]
    });
  }

  onSubmit() {
    if (this.cadastroForm.invalid) {
      this.cadastroForm.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    const formValues = this.cadastroForm.value;

    const novoUsuario: User = {
      name: formValues.nome,
      email: formValues.email,
      password: formValues.senha,
      role: formValues.role
    };

    this.authService.register(novoUsuario).subscribe({
      next: (response: any) => {
        this.isLoading = false;
        alert('Sucesso: Cadastro realizado! Faça login para continuar.');
        this.router.navigate(['/login']);
      },
      error: (err: any) => {
        this.isLoading = false;
        const mensagemErro = err.error?.message || 'Falha ao realizar o cadastro. Verifique os dados.';
        alert(`Erro: ${mensagemErro}`);
        console.error(err);
      }
    });
  }
}