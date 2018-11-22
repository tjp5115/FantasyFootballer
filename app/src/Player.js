import React, { Component } from 'react';
import './Player.css';

class Player extends Component {
	constructor(props){
		super(props);
		if(props.header){
			this.state = {
				header : props.header,
			}
		}else{
			this.state = {
				firstName : props.player.firstName,
				lastName : props.player.lastName,
				tier: props.player.tier,
			};
		}
	}
	render() {
		const { firstName, lastName, header, tier} = this.state
		if (header){
			return <tr><th>Tier</th><th>Name</th></tr>
		}else{
			return <tr><td>{tier}</td><td>{firstName} {lastName}</td></tr>
		}
	}
}

export default Player;
