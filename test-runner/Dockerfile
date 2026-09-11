FROM mcr.microsoft.com/playwright:v1.63.0-noble
WORKDIR /runner
COPY package*.json ./
RUN npm install
COPY . .
CMD ["npm","test"]
