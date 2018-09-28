export class Team {
    id: number;
    name: string;
    intelligence: number;
    strength: number;
    speed: number;
    durability: number;
    power: number;
    combat: number;
    user: {
        id: number;
        username: string;
        password: string;
        email: string;
        role: string;
    };
    heroes: [];
}