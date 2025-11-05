# Monarca Frontend - Angular 18

Modern Angular 18 frontend for the Monarca Beauty Salon appointment booking system.

## 🚀 Technology Stack

- **Angular 18.2** - Modern web framework with standalone components
- **TypeScript 5.5** - Type-safe development
- **Tailwind CSS 3.4** - Utility-first CSS framework
- **RxJS 7.8** - Reactive programming
- **Angular Router** - Client-side routing with lazy loading

## 📋 Prerequisites

- **Node.js** 20.x or higher
- **npm** 10.x or higher
- **Angular CLI** 18.x (optional, but recommended)

```bash
# Install Angular CLI globally (optional)
npm install -g @angular/cli
```

## 🛠️ Installation

```bash
# Install dependencies
npm install

# Note: Use --legacy-peer-deps if you encounter peer dependency issues
npm install --legacy-peer-deps
```

## 🏃 Development

### Start Development Server

```bash
# Using npm script
npm start

# Or using Angular CLI
ng serve

# With custom port
ng serve --port 4201
```

The application will be available at `http://localhost:4200/`

## 🏗️ Build

### Production Build

```bash
# Optimized build for production
npm run build

# Output will be in dist/monarca-frontend/browser/
```

The production build includes AOT compilation, tree shaking, minification and optimization.

## 📁 Key Directories

```
src/app/
├── core/                    # Services, guards, interceptors
├── features/                # Feature pages (lazy-loaded)
├── shared/                  # Shared components and models
└── environments/            # Environment configurations
```

## 🔐 Environment Configuration

Update `src/environments/environment.ts` with your backend URL:

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api',
  culqiPublicKey: 'pk_test_your_key'
};
```

## 🔑 Authentication

JWT-based authentication with role-based access control (admin, receptionist, stylist, client).

## 📚 Documentation

See `FRONTEND_MISSING.md` and `FRONTEND_COMPLETION.md` for migration details.

**Built with ❤️ using Angular 18**
