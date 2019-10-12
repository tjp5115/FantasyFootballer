import React, { Component } from 'react';
import './Lineup.css';

class Lineup extends Component {

	constructor(props) {
	super(props);
		this.state = {
		  error: null,
		  leagueId: props.leagueId,
		  teamId: props.teamId,
		  leagueName: props.leagueName,
		  leagueType: props.leagueType,
		  isLoaded: null,
		  items: [],
		};
	}

	componentDidMount(){
		updateWithPlayers(this);
	}

	componentDidUpdate() {
	const { leagueId, teamId, isLoaded } = this.state;

	if ( this.props.leagueId === leagueId ||  this.props.teamId === teamId ){
	  return;
	}

	if( isLoaded ){
	  this.setState({
	    isLoaded: false
	  })
	}

	updateWithPlayers(this);
	}

	render(){
		const { error, isLoaded, items } = this.state;

	    if (error) {
	      return <div>Error: {error.message}</div>;
	    } else if (!isLoaded) {
	      return <div><h1> Possible Trades </h1><p>Loading...</p></div>;
	    } else {
			return <div>
			  <h1> Best Lineup </h1>
			  <table>
			  	<tr><th>Tier</th><th>Name</th><th>Position</th></tr>
			  	{items.map( player =>
			      	<tr> 
			      		<td>{player.tier}</td><td>{player.firstName} {player.lastName}</td><td>{player.position}</td>
			      	</tr>
			  	)}
			  </table>
			</div>
		}
	}

}

function updateWithPlayers(trade){
	const {leagueId, teamId, leagueType} = trade.props;
	fetch("http://localhost:8080/footballer/espn/lineup?leagueID="+leagueId+"&teamID="+teamId+"&leagueType="+leagueType)
	.then(res => res.json())
	.then(
	  (result) => {
	    trade.setState({
	      isLoaded: true,
	      items: result.data.items,
	      leagueId: leagueId,
	      teamId: teamId
	    });
	  },
	  // Note: it's important to handle errors here
	  // instead of a catch() block so that we don't swallow
	  // exceptions from actual bugs in components.
	  (error) => {
	    trade.setState({
	      isLoaded: true,
	      error
	    });
	  }
	)
}

export default Lineup;
