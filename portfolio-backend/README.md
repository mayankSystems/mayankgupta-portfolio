# 🚀 Mayank Gupta — Portfolio Backend

Spring Boot 3.2 REST API backend for portfolio website with SendGrid email integration, rate limiting, and health checks.

---

## 📁 Project Structure

```
portfolio-backend/
├── src/
│   └── main/
│       ├── java/dev/mayank/portfolio/
│       │   ├── PortfolioApplication.java
│       │   ├── controller/
│       │   │   ├── ContactController.java     ← POST /api/contact (with rate limiting)
│       │   │   ├── ResumeController.java      ← GET /api/resume/download
│       │   │   └── PortfolioController.java   ← GET /api/health, /api/info
│       │   ├── service/
│       │   │   └── ContactService.java        ← SendGrid email integration
│       │   ├── model/
│       │   │   ├── ContactRequest.java        ← Validated request body
│       │   │   └── ApiResponse.java           ← Consistent API wrapper
│       │   └── config/
│       │       └── CorsConfig.java            ← CORS configuration
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

| Method | Endpoint              | Description                    | Rate Limit |
|--------|-----------------------|--------------------------------|-----------|
| GET    | `/api/health`         | Health check (Railway uses this) | None |
| GET    | `/api/info`           | Portfolio owner info           | None |
| POST   | `/api/contact`        | Contact form submission        | 3 requests/hour per IP |
| GET    | `/api/resume/download`| Download resume PDF            | None |

---

## 📋 API Response Format

All endpoints return a consistent `ApiResponse<T>` wrapper:

```json
{
  "success": true,
  "message": "Success message",
  "data": { /* endpoint-specific data */ },
  "timestamp": "2026-04-20T12:30:45"
}
```

### Contact Form Request Body

```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "subject": "Job Opportunity",
  "message": "Hey Mayank, I have an exciting opportunity for you!"
}
```

**Validation Rules:**
- `name`: 2-100 characters (required)
- `email`: Valid email format (required)
- `subject`: Max 200 characters (optional)
- `message`: 10-2000 characters (required)

---

## 🔧 Local Development

### Prerequisites
- Java 17+
- Maven 3.8+
- SendGrid API Key

### Setup

1. Clone the repository:
```bash
git clone https://github.com/mayankSystems/mayankgupta-portfolio.git
cd portfolio-backend
```

2. Set environment variables:
```bash
export SENDGRID_API_KEY=your-sendgrid-api-key
export PORT=8080
export FRONTEND_URL=http://localhost:5173
```

3. Run the backend:
```bash
mvn spring-boot:run
```

Backend runs at: `http://localhost:8080`

### Test the API

**Health Check:**
```bash
curl http://localhost:8080/api/health
```

**Portfolio Info:**
```bash
curl http://localhost:8080/api/info
```

**Contact Form:**
```bash
curl -X POST http://localhost:8080/api/contact \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "subject": "Job Opportunity",
    "message": "Hey Mayank, I have an exciting opportunity for you!"
  }'
```

---

## 🌐 Deployment Guide

### Prerequisites
- SendGrid account with API key
- Railway account (recommended) or similar deployment platform

---

### Step 1 — Set Up SendGrid

1. Go to sendgrid.com and create an account
2. Navigate to Settings → API Keys
3. Create a new API key
4. Copy the API key (you'll need this for deployment)

---

### Step 2 — Deploy Backend to Railway

1. Go to railway.app and sign in with GitHub
2. Click New Project → Deploy from GitHub Repo
3. Select the mayankgupta-portfolio repository
4. Set the root directory to portfolio-backend
5. Add these Environment Variables in Railway dashboard:

```
SENDGRID_API_KEY=your-sendgrid-api-key
PORT=8080
FRONTEND_URL=https://your-portfolio.vercel.app
```

6. Update the portfolio config in application.properties:

```properties
portfolio.email.to=your-email@example.com
portfolio.email.from=noreply@yourportfolio.com
portfolio.cors.allowed-origins=https://your-portfolio.vercel.app
```

7. Railway will auto-detect the railway.toml and deploy
8. Note your Railway URL: https://portfolio-api-production.up.railway.app

---

### Step 3 — Deploy Frontend to Vercel

1. Push frontend/index.html and photo.jpg to a GitHub repo
2. Go to vercel.com → New Project → Import your GitHub repo
3. Set Framework to "Other" (it's a static HTML file)
4. Set Output Directory to . (root)
5. Deploy!

6. Update API_BASE in frontend/index.html:
```javascript
const API_BASE = 'https://your-api.up.railway.app';
```

7. Update FRONTEND_URL in Railway environment variables with your Vercel URL:
```
FRONTEND_URL=https://your-portfolio.vercel.app
```

---

## 📧 Email Flow

When someone submits the contact form:

1. **Request Validation**: Email, name, message are validated
2. **Rate Limit Check**: 3 submissions per IP per hour (enforced)
3. **Notification Email** sent to portfolio.email.to with full message details
4. **Confirmation Email** sent to the sender automatically
5. **API Response**: Success/error message returned to frontend

### Email Styling

Both notification and confirmation emails use:
- Dark theme (matches modern portfolio aesthetic)
- HTML formatting with inline CSS
- Timestamp and metadata included

---

## 🔒 Security Features

| Feature | Details |
|---------|---------|
| Rate Limiting | 3 contact form submissions per IP per hour (Bucket4j) |
| Input Validation | All fields validated with Jakarta Bean Validation |
| CORS | Configured to allow only your frontend domain |
| Global Exception Handling | No sensitive stack traces exposed to clients |
| Docker Security | Non-root user execution |
| SendGrid | Industry-standard email delivery with authentication |

---

## 🛠️ Tech Stack

| Layer        | Technology                  |
|-------------|---------------------------|
| Runtime | Java 17                    |
| Framework | Spring Boot 3.2.5          |
| Build   | Maven 3.8+                |
| Email   | SendGrid Java SDK v4.9.3  |
| Rate Limiting | Bucket4j v8.10.1     |
| Validation | Jakarta Bean Validation   |
| Logging | SLF4J + Logback           |
| Utilities | Lombok                   |
| Deploy | Docker + Railway          |

---

## 📦 Dependencies

Key dependencies (see pom.xml for complete list):

```xml
<!-- Spring Boot Web & Validation -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

<!-- SendGrid Email API -->
<dependency>
    <groupId>com.sendgrid</groupId>
    <artifactId>sendgrid-java</artifactId>
    <version>4.9.3</version>
</dependency>

<!-- Rate Limiting -->
<dependency>
    <groupId>com.bucket4j</groupId>
    <artifactId>bucket4j-core</artifactId>
    <version>8.10.1</version>
</dependency>

<!-- Actuator (Health checks) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

---

## 🚀 Troubleshooting

### Email not sending
- Verify SENDGRID_API_KEY is set correctly
- Check SendGrid account for API key restrictions
- Review logs: tail -f logs/portfolio.log

### Rate limit errors (429)
- Normal behavior when limit exceeded
- Client can retry after 1 hour or from a different IP
- Configure limit in ContactController.java line 58

### CORS errors
- Verify FRONTEND_URL matches your deployed frontend URL
- Update portfolio.cors.allowed-origins in application.properties

### Health check failing on Railway
- Ensure SENDGRID_API_KEY environment variable is set
- Check that spring.mail.test-connection=false is in application.properties

---

## 📝 Configuration Reference

### Application Properties

```properties
# Server
server.port=${PORT:8080}

# Portfolio Config
portfolio.email.to=ce.mayank8@gmail.com
portfolio.email.from=noreply@mayank.dev
portfolio.resume.path=classpath:static/resume.pdf

# CORS
portfolio.cors.allowed-origins=http://localhost:5173,${FRONTEND_URL}

# Actuator (Health checks)
management.endpoints.web.exposure.include=health,info
management.endpoint.health.show-details=always

# Logging
logging.level.dev.mayank=INFO
```

---

Built with ❤️ by Mayank Gupta · ce.mayank8@gmail.com