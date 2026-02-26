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
COPY nginx.conf /etc/nginx/conf.d/default.conf
RUN chown -R appuser:appgroup /usr/share/nginx/html

USER appuser

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
