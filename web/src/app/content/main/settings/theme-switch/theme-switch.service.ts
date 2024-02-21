import { Injectable } from '@angular/core';

export enum ThemeType {
  LIGHT = 'LIGHT',
  DARK = 'DARK',
}

@Injectable({
  providedIn: 'root',
})
export class ThemeSwitchService {
  private _isDarkMode: boolean = false;

  set setDarkMode(value: boolean) {
    this._isDarkMode = value;

    if (value) {
      this.updateSelectedTheme(ThemeType.DARK);
    } else this.updateSelectedTheme(ThemeType.LIGHT);
  }

  get isDarkMode(): boolean {
    return this._isDarkMode;
  }

  private STORAGE_KEY: string = 'theme';

  public checkTheme(): void {
    if (this.checkIfInsideLocalStorageExistSelectedTheme()) {
      const theme: ThemeType = this.getSelectedThemeFromStorage();
      this.setDarkMode = theme === ThemeType.DARK;
    } else {
      const theme: ThemeType = this.checkUserSystemPreferences();
      this.setDarkMode = theme === ThemeType.DARK;
    }
  }

  private checkIfInsideLocalStorageExistSelectedTheme(): boolean {
    return !!localStorage.getItem(this.STORAGE_KEY);
  }

  private getSelectedThemeFromStorage(): ThemeType {
    const theme = localStorage.getItem(this.STORAGE_KEY);
    return theme as ThemeType;
  }

  private updateSelectedTheme(theme: ThemeType) {
    localStorage.setItem(this.STORAGE_KEY, theme);
    document.body.classList.toggle('dark', theme === ThemeType.DARK);
  }

  private checkUserSystemPreferences(): ThemeType {
    const prefersDark = window.matchMedia('(prefers-color-scheme: dark)');
    if (prefersDark?.matches) {
      return ThemeType.DARK;
    } else return ThemeType.LIGHT;
  }
}
