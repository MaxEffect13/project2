import { Component, OnInit } from '@angular/core';
import { PostService } from '../../services/post.service';
import { Team } from '../../models/team';

@Component({
  selector: 'app-team-battle',
  templateUrl: './team-battle.component.html',
  styleUrls: ['./team-battle.component.css']
})
export class TeamBattleComponent implements OnInit {

  constructor(private postService: PostService) { }

  ngOnInit() {
    this.result()
  }

//   team: Team= {
//     id: undefined,
//     name: undefined,
//     intelligence: undefined,
//     strength: undefined,
//     speed: undefined,
//     durability: undefined,
//     power: undefined,
//     combat: undefined,
//     user: {
//         id: undefined,
//         username: undefined,
//         password: undefined,
//         email: undefined,
//         role: undefined
//     },
//     heroes: []
// }
  winner: string;
  result() {
    this.postService.battle().then((x) => { this.winner = x.name});
  }
}
