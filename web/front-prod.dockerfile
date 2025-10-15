# build stage
FROM node:18-alpine AS build-stage
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .

# Use production Keycloak config
ARG KEYCLOAK_CONFIG=keycloak-prod.json
RUN cp public/${KEYCLOAK_CONFIG} public/keycloak.json

RUN npm run build --ignore-ts-errors

# production stage
FROM nginx:stable-alpine AS production-stage
COPY --from=build-stage /app/dist /usr/share/nginx/html
COPY ./nginx.conf /etc/nginx/conf.d/custom.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
