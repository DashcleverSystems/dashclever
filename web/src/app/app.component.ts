import { Component, OnInit, SkipSelf } from '@angular/core';
import { CoreTranslateService } from './core/translate/core-translate.service';
import { environment } from '@env/environments';
import { Language } from './shared/enums/languages';
import { ThemeSwitchService } from '@content/main/settings/theme-switch/theme-switch.service';
import { CoreService } from './core/store/core-store.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  constructor(
    private coreTranslateService: CoreTranslateService,
    @SkipSelf() private themeService: ThemeSwitchService,
    @SkipSelf() private coreService: CoreService,
  ) {}

  ngOnInit(): void {
    this.coreTranslateService.init(
      environment.defaultLanguage as Language,
      environment.supportedLanguages as Language[],
    );

    this.themeService.checkTheme();
  }
}
