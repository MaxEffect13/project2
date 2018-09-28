import { Component, OnInit } from '@angular/core';
import { PostService } from '../../services/post.service';
import { Router } from '@angular/router';
import { Team } from '../../models/team';
@Component({
  selector: 'app-team-search',
  templateUrl: './team-search.component.html',
  styleUrls: ['./team-search.component.css']
})
export class TeamSearchComponent implements OnInit {

  constructor(private postService: PostService, private router: Router) { }

  ngOnInit() {
    this.getTeams();
  }

  teams: any[];
  getTeams() {
    let posts;
    this.postService.getTeams().then((allPosts) => {posts = allPosts; this.teams = posts; console.log(this.teams)});
  }

  team: Team= {
    id: undefined,
    name: undefined,
    intelligence: undefined,
    strength: undefined,
    speed: undefined,
    durability: undefined,
    power: undefined,
    combat: undefined,
    user: {
        id: undefined,
        username: undefined,
        password: undefined,
        email: undefined,
        role: undefined
    },
    heroes: []
}
  battleSearch(id: number) {
    localStorage.setItem("team2", id.toString());
    window.location.replace("http://localhost:4200/teamBattle");
  }
}
