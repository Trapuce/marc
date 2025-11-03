# build stage
FROM node:18-alpine AS build-stage
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
ARG VITE_API_URL=https://backend.marc.trapuce.tech/api
ENV VITE_API_URL=$VITE_API_URL
RUN npm run build --ignore-ts-errors

# production stage
FROM nginx:stable-alpine AS production-stage
COPY --from=build-stage /app/dist /usr/share/nginx/html
COPY ./nginx.conf /etc/nginx/conf.d/custom.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]