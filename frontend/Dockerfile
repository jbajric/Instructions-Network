FROM node:12-alpine
WORKDIR /frontend
COPY ./package.json .
RUN npm install
COPY . /frontend
CMD npm build
CMD npm audit fix
EXPOSE 3000
CMD npm start
