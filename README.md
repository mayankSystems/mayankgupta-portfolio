# 🚀 Mayank Gupta — Portfolio Website

> **Live site:** [mayank-gupta.vercel.app](https://mayank-gupta.vercel.app) *(update after deploy)*  
> **Backend API:** [portfolio-api.up.railway.app](https://portfolio-api.up.railway.app/api/health) *(update after deploy)*

A full-stack developer portfolio built with a **Spring Boot REST API** backend and a **vanilla HTML/CSS/JS** frontend — deployed on **Vercel + Railway** for free.

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Frontend | HTML5 · CSS3 · Vanilla JS · JetBrains Mono · Syne fonts |
| Backend | Java 17 · Spring Boot 3.2 · Spring Mail |
| Rate Limiting | Bucket4j (3 requests/IP/hour) |
| Validation | Jakarta Bean Validation |
| Deploy (FE) | Vercel |
| Deploy (BE) | Railway |
| Container | Docker (multi-stage, non-root) |

---

## 📁 Project Structure

```
mayankgupta-portfolio/
├── portfolio/                  ← Frontend
│   ├── index.html              ← Complete single-file portfolio
│   ├── photo.jpg               ← Profile photo
│   └── resume.pdf              ← Resume (add before deploy)
│
└── portfolio-backend/          ← Spring Boot REST API
    ├── src/
    │   └── main/java/dev/mayank/portfolio/
    │       ├── controller/
    │       │   ├── ContactController.java      ← POST /api/contact
    │       │   ├── ResumeController.java       ← GET /api/resume/download
    │       │   └── PortfolioController.java    ← GET /api/health
    │       ├── service/
    │       │   └── ContactService.java         ← Email via JavaMailSender
    │       ├── model/
    │       │   ├── ContactRequest.java
    │       │   └── ApiResponse.java
    │       └── config/
    │           ├── CorsConfig.java
    │           └── GlobalExceptionHandler.java
    ├── Dockerfile
    ├── railway.toml
    └── pom.xml
```

---

## ⚙️ API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/health` | Health check |
| `GET` | `/api/info` | Portfolio owner info |
| `POST` | `/api/contact` | Contact form (sends email) |
| `GET` | `/api/resume/download` | Download resume PDF |

---

## 🏃 Run Locally

### Frontend
```bash
# No build step needed — just serve statically
python3 -m http.server 3000 --directory portfolio/
# Open: http://localhost:3000
```

### Backend
```bash
cd portfolio-backend

# Set environment variables
export MAIL_USERNAME=your@gmail.com
export MAIL_PASSWORD=your-gmail-app-password

mvn spring-boot:run
# API running at: http://localhost:8080
```

### Test the API
```bash
# Health check
curl http://localhost:8080/api/health

# Contact form
curl -X POST http://localhost:8080/api/contact \
  -H "Content-Type: application/json" \
  -d '{"name":"Test","email":"test@example.com","message":"Hello Mayank!"}'
```

---

## 🌐 Deployment

### Backend → Railway
1. Connect GitHub repo to [railway.app](https://railway.app)
2. Set root directory to `portfolio-backend`
3. Add environment variables:
```
MAIL_USERNAME=ce.mayank8@gmail.com
MAIL_PASSWORD=<gmail-app-password>
CONTACT_EMAIL=ce.mayank8@gmail.com
FRONTEND_URL=https://your-site.vercel.app
PORT=8080
```

### Frontend → Vercel
1. Connect GitHub repo to [vercel.com](https://vercel.com)
2. Set root directory to `portfolio`
3. Framework: **Other** (static HTML)
4. Update `API_BASE` in `index.html` with your Railway URL

---

## 🔒 Security
- Rate limiting: max 3 contact requests per IP per hour
- Input validation on all fields
- CORS scoped to Vercel domain only
- Docker container runs as non-root user
- No secrets in codebase — all via environment variables

---

## 👨‍💻 Author

**Mayank Gupta** — Senior Backend Engineer  
📧 ce.mayank8@gmail.com  
🔗 [LinkedIn](https://linkedin.com/in/mayanksystems) · [GitHub](https://github.com/mayankSystems)
