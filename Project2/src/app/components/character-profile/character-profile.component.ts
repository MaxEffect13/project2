import { Component, OnInit } from '@angular/core';
import { PostService } from '../../services/post.service';
import { ActivatedRoute } from '@angular/router';
import { Chart } from 'chart.js';
import { SessionService } from '../../services/session.service';

@Component({
  selector: 'app-character-profile',
  templateUrl: './character-profile.component.html',
  styleUrls: ['./character-profile.component.css']
})
export class CharacterProfileComponent implements OnInit {
  dataSource: any;
  teamSelect: string;
  constructor(private postService: PostService, private sessionService: SessionService, private route: ActivatedRoute) {}

  ngOnInit() {
    // this.route.params.subscribe(param =>
    //   this.hero.id = param['id'])
    //   console.log(this.hero.id);
    //   this.getHero(Number(this.hero.id));
    this.getHero(window.location.search.substring(4));
    this.sessionCheck();
    this.getTeams();
  }
  hero: any = {
    id: undefined,
    name: undefined,
    powerstats: undefined,
    biography: undefined,
    appearance: undefined,
    work: undefined,
    connections: undefined,
    image: undefined
  }

  getHero(id: string) {
    console.log(id);
    this.postService.getHero(id).then((res) => { this.hero = res; console.log(this.hero)}).catch((e)=> console.log(e));
  }

  session : boolean
  sessionCheck() {
    this.session = this.sessionService.sessionCheck();
  }

  logout() {
    localStorage.clear();
    window.location.replace("http://localhost:4200/home");
  }

  teams: any[];
  getTeams() {
    let posts;
    this.postService.getUserTeams(localStorage.getItem("userID")).then((allPosts) => {posts = allPosts; this.teams = posts; console.log(this.teams)});
  }

  addToTeam(teamId: number, heroId: number) {
    console.log(teamId);
    console.log(heroId);
    console.log(localStorage.getItem("userID"));
    this.postService.addToTeam(teamId, heroId, localStorage.getItem("userID")).subscribe();
  }
}
