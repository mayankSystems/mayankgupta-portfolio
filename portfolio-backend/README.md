# 🚀 Mayank Gupta — Portfolio Website

Full-stack portfolio with a **React/HTML frontend** and **Spring Boot REST API backend**.

---

## 📁 Project Structure

```
portfolio/
├── frontend/
│   ├── index.html          ← Complete portfolio (single file, production ready)
│   └── photo.jpg           ← Profile photo
│
└── backend/  (portfolio-backend/)
    ├── src/
    │   └── main/
    │       ├── java/dev/mayank/portfolio/
    │       │   ├── PortfolioApplication.java
    │       │   ├── controller/
    │       │   │   ├── ContactController.java     ← POST /api/contact
    │       │   │   ├── ResumeController.java      ← GET /api/resume/download
    │       │   │   └── PortfolioController.java   ← GET /api/health, /api/info
    │       │   ├── service/
    │       │   │   └── ContactService.java        ← Email via JavaMailSender
    │       │   ├── model/
    │       │   │   ├── ContactRequest.java        ← Validated request body
    │       │   │   └── ApiResponse.java           ← Consistent API wrapper
    │       │   └── config/
    │       │       ├── CorsConfig.java            ← CORS for Vercel frontend
    │       │       └── GlobalExceptionHandler.java
    │       └── resources/
    │           ├── application.properties
    │           └── static/
    │               └── resume.pdf  ← PUT YOUR RESUME HERE
    ├── pom.xml
    ├── Dockerfile
    └── railway.toml
```

---

## ⚙️ Backend API Endpoints

| Method | Endpoint              | Description                    |
|--------|-----------------------|--------------------------------|
| GET    | /api/health           | Health check (Railway uses this)|
| GET    | /api/info             | Portfolio owner info           |
| POST   | /api/contact          | Contact form submission (email)|
| GET    | /api/resume/download  | Download resume PDF            |

---

## 🔧 Local Development

### Prerequisites
- Java 17+
- Maven 3.8+
- Gmail account with App Password enabled

### Run Backend Locally

```bash
cd portfolio-backend

# Set environment variables
export MAIL_USERNAME=ce.mayank8@gmail.com
export MAIL_PASSWORD=your-gmail-app-password

mvn spring-boot:run
```

Backend runs at: `http://localhost:8080`

### Test the Contact API
```bash
curl -X POST http://localhost:8080/api/contact \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "subject": "Job Opportunity",
    "message": "Hey Mayank, I have an exciting opportunity!"
  }'
```

### Open Frontend
Just open `frontend/index.html` in your browser (no build step needed).
Update `API_BASE` in the JS to `http://localhost:8080` for local testing.

---

## 🌐 Deployment Guide

### Step 1 — Deploy Backend to Railway

1. Go to [railway.app](https://railway.app) and sign in with GitHub
2. Click **New Project → Deploy from GitHub Repo**
3. Select this repository
4. Set the **root directory** to `portfolio-backend`
5. Add these **Environment Variables** in Railway dashboard:

```
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=ce.mayank8@gmail.com
MAIL_PASSWORD=<your-gmail-app-password>
CONTACT_EMAIL=ce.mayank8@gmail.com
FROM_EMAIL=noreply@mayank.dev
FRONTEND_URL=https://your-portfolio.vercel.app
PORT=8080
```

6. Railway will auto-detect the `railway.toml` and deploy
7. Note your Railway URL: `https://portfolio-api-production.up.railway.app`

> **Gmail App Password**: Go to Google Account → Security → 2-Step Verification → App Passwords → Generate one for "Mail"

---

### Step 2 — Deploy Frontend to Vercel

1. Push `frontend/index.html` and `photo.jpg` to a GitHub repo
2. Go to [vercel.com](https://vercel.com) → New Project → Import your GitHub repo
3. Set **Framework** to "Other" (it's a static HTML file)
4. Set **Output Directory** to `.` (root)
5. Deploy!

6. Update `API_BASE` in `index.html` with your Railway URL:
```javascript
const API_BASE = 'https://your-api.up.railway.app';
```

7. Update `FRONTEND_URL` in Railway env vars with your Vercel URL:
```
FRONTEND_URL=https://your-portfolio.vercel.app
```

---

### Step 3 — Resume Download

Place your resume PDF at:
```
portfolio-backend/src/main/resources/static/resume.pdf
```

Also add `resume.pdf` next to `index.html` in the frontend folder for the direct download link.

---

## 🔒 Security Features

- **Rate limiting**: Max 3 contact form submissions per IP per hour (Bucket4j)
- **Input validation**: All fields validated with Jakarta Bean Validation
- **CORS**: Only allows requests from your Vercel domain
- **Global exception handling**: No stack traces exposed to clients
- **Non-root Docker**: Runs as a dedicated `portfolio` user

---

## 📧 Email Flow

When someone submits the contact form:
1. Frontend calls `POST /api/contact`
2. Spring Boot validates the payload
3. **Notification email** sent to `ce.mayank8@gmail.com` with full message
4. **Confirmation email** sent back to the sender automatically
5. API returns success/error response shown in the UI

---

## 🛠️ Tech Stack

| Layer      | Technology                          |
|------------|-------------------------------------|
| Frontend   | HTML5 · CSS3 · Vanilla JS           |
| Backend    | Java 17 · Spring Boot 3.2           |
| Email      | Spring Mail · JavaMailSender        |
| Rate Limit | Bucket4j                            |
| Deploy FE  | Vercel (free)                       |
| Deploy BE  | Railway ($5 free credits/month)     |
| Container  | Docker (multi-stage build)          |

---

Built with ❤️ by Mayank Gupta · ce.mayank8@gmail.com
