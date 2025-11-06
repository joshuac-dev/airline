# Airline Club Wiki - Static Documentation Site

This is a modern, responsive static documentation site for the Airline Club game, migrated from scraped Fandom wiki content.

## 🎨 Features

- **Modern Design**: Clean, professional interface with semantic HTML5
- **Dark Mode**: Toggle between light and dark themes with localStorage persistence
- **Responsive**: Mobile-friendly layouts that adapt to all screen sizes
- **Fast Navigation**: Fixed navigation bar with breadcrumbs on every page
- **Interactive Elements**: Tabbed image galleries on aircraft pages
- **Organized Structure**: Content categorized into Aircraft, Mechanics, and World sections

## 📁 Directory Structure

```
/docs/
├── index.html              # Landing page with category overview
├── assets/
│   ├── css/
│   │   └── style.css       # Global styles with CSS variables and dark mode
│   ├── js/
│   │   └── main.js         # Dark mode toggle and image tab functionality
│   └── images/
│       └── aircraft/       # 259 aircraft images (only used images copied)
├── aircraft/               # 80 aircraft pages organized by manufacturer
│   ├── airbus/            # Airbus aircraft (28 models)
│   ├── boeing/            # Boeing aircraft (32 models)
│   ├── bombardier/        # Bombardier aircraft (3 models)
│   ├── embraer/           # Embraer aircraft (2 models)
│   ├── comac/             # COMAC aircraft (2 models)
│   ├── antonov/           # Antonov aircraft (1 model)
│   ├── ilyushin/          # Ilyushin aircraft (1 model)
│   ├── concorde/          # Concorde (1 model)
│   └── other/             # Other manufacturers (8 models)
├── mechanics/             # 46 game mechanics and guide pages
└── world/                 # 6 world-related pages (airports, maps, etc.)
```

## 🚀 Quick Start

### Viewing Locally

Simply open `index.html` in any modern web browser, or serve with a local web server:

```bash
# Python 3
python3 -m http.server 8080

# Python 2
python -m SimpleHTTPServer 8080

# Node.js (npx)
npx http-server -p 8080

# PHP
php -S localhost:8080
```

Then navigate to `http://localhost:8080`

### Deploying

This site is pure HTML/CSS/JavaScript with no build step required. You can deploy to:

- **GitHub Pages**: Push to a `gh-pages` branch or configure in repository settings
- **Netlify**: Drag and drop the `/docs` folder or connect to Git
- **Vercel**: Import the repository and set output directory to `docs`
- **Any static host**: Upload the `/docs` folder contents

## 📄 Page Structure

### Aircraft Pages

Each aircraft page includes:
- **Image Gallery**: Tabbed images (e.g., real photos and in-game renders)
- **Info Box**: Family, manufacturer, and related aircraft
- **Game Stats Grid**: Base cost, capacity, range, fuel burn, lifespan, speed, runway requirements, delivery time, turnaround time
- **Description**: Overview of the aircraft
- **Trivia**: Interesting facts (when available)
- **External Links**: Wikipedia and manufacturer sites

### Mechanics Pages

Game guides and mechanics covering:
- Getting started tutorials
- Route planning and optimization
- Economics and profit management
- Airline management features
- Game systems and mechanics

### World Pages

Information about:
- Airports and their features
- World map and routes
- Game world features

## 🎨 Customization

### Changing Colors

All colors are defined as CSS variables in `assets/css/style.css`:

```css
:root {
    --accent-primary: #3498db;    /* Main accent color */
    --bg-primary: #ffffff;        /* Main background */
    --text-primary: #333;         /* Main text color */
    /* ... more variables */
}

[data-theme="dark"] {
    --accent-primary: #4a9eff;    /* Dark mode accent */
    --bg-primary: #1e1e1e;        /* Dark mode background */
    --text-primary: #e0e0e0;      /* Dark mode text */
    /* ... more variables */
}
```

### Adding New Pages

1. Create HTML file in appropriate directory (`aircraft/`, `mechanics/`, or `world/`)
2. Use existing pages as templates
3. Ensure proper navigation links and breadcrumbs
4. Link to `../../assets/css/style.css` and `../../assets/js/main.js` (adjust path as needed)
5. Include CC-BY-SA attribution in footer

## 📊 Migration Statistics

- **Source Files**: 132 HTML files from `/souredata`
- **Generated Pages**: 133 (including index.html)
- **Images**: 259 of 260 referenced images copied (20 unused images left in source)
- **Categories**:
  - Aircraft: 80 pages
  - Mechanics: 46 pages
  - World: 6 pages

## 🔧 Technical Details

### CSS Features
- CSS Custom Properties (variables) for theming
- CSS Grid for responsive layouts
- Flexbox for navigation and cards
- Mobile-first responsive design
- Smooth transitions for dark mode

### JavaScript Features
- Dark mode toggle with localStorage persistence
- Image tab switcher
- Smooth scrolling for anchor links
- Current page highlighting in navigation
- No external dependencies (vanilla JS)

### Browser Support
- Modern browsers (Chrome, Firefox, Safari, Edge)
- IE11 not supported (uses CSS Grid and CSS Variables)

## 📝 License

Original content from [Airline Club Wiki](https://airline-club.fandom.com/). Licensed under [CC-BY-SA](https://creativecommons.org/licenses/by-sa/3.0/).

For more information about the Airline Club game, visit [www.airline-club.com](https://www.airline-club.com/)

## 🛠️ Development

### Migration Script

The site was generated using `migrate_site.py` which:
1. Parses Fandom infobox data from source HTML
2. Extracts aircraft statistics
3. Categorizes pages into aircraft/mechanics/world
4. Organizes aircraft by manufacturer
5. Generates modern HTML with navigation and breadcrumbs
6. Copies only referenced images
7. Creates consistent page structure

### File Naming

- URLs use lowercase with hyphens (e.g., `airbus-a320.html`)
- Organized by manufacturer for aircraft
- Flat structure for mechanics and world pages

## 📞 Support

For issues with the documentation site, please refer to the main repository.

For game-related questions, visit the [Airline Club](https://www.airline-club.com/) website.
