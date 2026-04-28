FROM node:14-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
ARG REACT_APP_EMPLOYEE_API_URL=http://localhost:8080/api/v1/employees
ENV REACT_APP_EMPLOYEE_API_URL=$REACT_APP_EMPLOYEE_API_URL
RUN npm run build

FROM nginx:1.25-alpine
COPY --from=build /app/build /usr/share/nginx/html
EXPOSE 80
