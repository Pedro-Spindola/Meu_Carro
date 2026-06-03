import { Injectable, signal, effect } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ThemeService {
  private currentTheme = signal<'light' | 'dark'>('dark');

  constructor() {
    const savedTheme = localStorage.getItem('theme') as 'light' | 'dark';
    if (savedTheme) {
      this.currentTheme.set(savedTheme);
    }

    // Effect to update the DOM whenever the theme changes
    effect(() => {
      const theme = this.currentTheme();
      document.documentElement.classList.remove('light-theme', 'dark-theme');
      document.documentElement.classList.add(`${theme}-theme`);
      localStorage.setItem('theme', theme);
    });

    this.syncWithSCSS();
  }

  private syncWithSCSS() {
    // Wait for styles to be parsed
    const themeFromCSS = getComputedStyle(document.documentElement)
      .getPropertyValue('--theme-name')
      .replace(/"/g, '')
      .trim();

    if (themeFromCSS === 'light' || themeFromCSS === 'dark') {
      this.currentTheme.set(themeFromCSS);
    }
  }

  get theme() {
    return this.currentTheme();
  }

  get logoUrl() {
    return this.currentTheme() === 'dark' ? 'logo_w.png' : 'logo.png';
  }

  setTheme(theme: 'light' | 'dark') {
    this.currentTheme.set(theme);
  }

  toggleTheme() {
    const newTheme = this.currentTheme() === 'light' ? 'dark' : 'light';
    this.setTheme(newTheme);
  }
}
