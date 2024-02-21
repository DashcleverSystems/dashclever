import { Component, SkipSelf } from '@angular/core';
import { ThemeSwitchService } from '@content/main/settings/theme-switch/theme-switch.service';

@Component({
  selector: 'app-theme-switch',
  template: `<div
    class="flex align-content-center justify-content-between mt-2"
  >
    <label for="theme-switch">{{
      'settings.switch-theme.switch' | translate
    }}</label>
    <p-inputSwitch
      [(ngModel)]="isDarkTheme"
      (onChange)="toggleTheme()"
      inputId="theme-switch"
    ></p-inputSwitch>
  </div>`,
})
export class ThemeSwitchComponent {
  isDarkTheme: boolean = this.service.isDarkMode;

  constructor(@SkipSelf() private service: ThemeSwitchService) {}

  toggleTheme(): void {
    this.service.setDarkMode = this.isDarkTheme;
  }
}
