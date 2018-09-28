import { Routes } from '@angular/router';
import { HomepageComponent } from './components/homepage/homepage.component';
import { CharacterProfileComponent } from './components/character-profile/character-profile.component';
import { ProfileComponent } from './components/profile/profile.component';
import { TeamSearchComponent } from './components/team-search/team-search.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { TeamBattleComponent } from './components/team-battle/team-battle.component';

export const appRoutes: Routes = [
    {
        path: 'home',
        component: HomepageComponent
    }, {
        path: 'heroProfile',
        component: CharacterProfileComponent
    }, {
        path: 'userProfile',
        component: ProfileComponent
    }, {
        path: 'teamSearch',
        component: TeamSearchComponent
    }, {
        path: 'login',
        component: LoginComponent
    }, {
        path: 'register',
        component: RegisterComponent
    }, {
        path: 'teamBattle',
        component: TeamBattleComponent
    }, {
        path: '**',
        pathMatch: 'full',
        redirectTo: 'home'
    }
]