import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { AppComponent } from './app.component';
import { HomepageComponent } from './components/homepage/homepage.component';
import { PostService } from './services/post.service';
import { appRoutes } from './routes';
import { RouterModule } from '@angular/router';
import { CharacterProfileComponent } from './components/character-profile/character-profile.component';
import { FilterPipe } from './pipes/filter.pipe';
import { ProfileComponent } from './components/profile/profile.component';
import { TeamSearchComponent } from './components/team-search/team-search.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { TeamBattleComponent } from './components/team-battle/team-battle.component';
@NgModule({
  declarations: [
    AppComponent,
    HomepageComponent,
    CharacterProfileComponent,
    FilterPipe,
    ProfileComponent,
    TeamSearchComponent,
    LoginComponent,
    RegisterComponent,
    TeamBattleComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes)
  ],
  providers: [PostService],
  bootstrap: [AppComponent]
})
export class AppModule { }
