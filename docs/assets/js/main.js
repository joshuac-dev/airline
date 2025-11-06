// Dark Mode Toggle
(function() {
    'use strict';
    
    // Initialize theme from localStorage or default to light
    const initTheme = () => {
        const savedTheme = localStorage.getItem('theme') || 'light';
        document.documentElement.setAttribute('data-theme', savedTheme);
        updateThemeIcon(savedTheme);
    };
    
    // Update theme icon
    const updateThemeIcon = (theme) => {
        const themeToggle = document.getElementById('theme-toggle');
        if (themeToggle) {
            themeToggle.textContent = theme === 'dark' ? '☀️' : '🌙';
            themeToggle.setAttribute('aria-label', 
                theme === 'dark' ? 'Switch to light mode' : 'Switch to dark mode'
            );
        }
    };
    
    // Toggle theme
    const toggleTheme = () => {
        const currentTheme = document.documentElement.getAttribute('data-theme');
        const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
        
        document.documentElement.setAttribute('data-theme', newTheme);
        localStorage.setItem('theme', newTheme);
        updateThemeIcon(newTheme);
    };
    
    // Initialize on DOM load
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initTheme);
    } else {
        initTheme();
    }
    
    // Add event listener to theme toggle button
    window.addEventListener('load', () => {
        const themeToggle = document.getElementById('theme-toggle');
        if (themeToggle) {
            themeToggle.addEventListener('click', toggleTheme);
        }
    });
    
    // Expose toggle function globally for inline onclick handlers (if needed)
    window.toggleTheme = toggleTheme;
})();

// Image Gallery Tab Switcher
function showImage(imageId) {
    // Hide all image contents
    const contents = document.querySelectorAll('.image-content');
    contents.forEach(content => content.classList.remove('active'));
    
    // Deactivate all tabs
    const tabs = document.querySelectorAll('.image-tab');
    tabs.forEach(tab => tab.classList.remove('active'));
    
    // Show selected image content
    const selectedContent = document.getElementById(imageId);
    if (selectedContent) {
        selectedContent.classList.add('active');
    }
    
    // Activate corresponding tab
    if (event && event.target) {
        event.target.classList.add('active');
    }
}

// Smooth scroll for anchor links
document.addEventListener('DOMContentLoaded', () => {
    const anchorLinks = document.querySelectorAll('a[href^="#"]');
    
    anchorLinks.forEach(link => {
        link.addEventListener('click', (e) => {
            const targetId = link.getAttribute('href');
            if (targetId === '#') return;
            
            const targetElement = document.querySelector(targetId);
            if (targetElement) {
                e.preventDefault();
                const navbarHeight = document.querySelector('.navbar')?.offsetHeight || 0;
                const targetPosition = targetElement.offsetTop - navbarHeight - 20;
                
                window.scrollTo({
                    top: targetPosition,
                    behavior: 'smooth'
                });
            }
        });
    });
});

// Mobile menu toggle (if needed in future)
function toggleMobileMenu() {
    const navbarNav = document.querySelector('.navbar-nav');
    if (navbarNav) {
        navbarNav.classList.toggle('active');
    }
}

// Utility: Highlight current page in navigation
document.addEventListener('DOMContentLoaded', () => {
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('.navbar-nav a');
    
    navLinks.forEach(link => {
        const linkPath = new URL(link.href).pathname;
        if (currentPath === linkPath || 
            (currentPath.includes('/aircraft/') && link.textContent.trim() === 'Aircraft') ||
            (currentPath.includes('/mechanics/') && link.textContent.trim() === 'Mechanics')) {
            link.style.fontWeight = 'bold';
            link.style.color = 'var(--accent-primary)';
        }
    });
});
