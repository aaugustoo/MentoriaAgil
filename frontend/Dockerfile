FROM node:20-alpine AS build
WORKDIR /app

COPY package*.json ./
RUN npm ci

COPY angular.json .
COPY tsconfig*.json .
COPY src ./src
RUN npm run build -- --configuration=production

FROM nginx:alpine

RUN addgroup -S appgroup && adduser -S appuser -G appgroup
COPY --from=build /app/dist/mentoria-agil/browser /usr/share/nginx/html
RUN chown -R appuser:appgroup /usr/share/nginx/html /var/cache/nginx /var/log/nginx /etc/nginx/nginx.conf
RUN touch /tmp/nginx.pid && chown appuser:appgroup /tmp/nginx.pid

USER appuser

EXPOSE 8081
CMD ["nginx", "-g", "daemon off;", "-c", "/etc/nginx/nginx.conf"]
