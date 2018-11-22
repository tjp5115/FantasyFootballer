import React, { Component } from 'react';
import './Trade.css';
import Player from './Player';

class Trade extends Component {

  constructor(props) {
    super(props);
    this.state = {
      error: null,
      leagueId: props.leagueId,
      teamId: props.teamId,
      leagueName: props.leagueName,
      isLoaded: false,
      items: [],
    };
  }

  componentDidMount() {
    const { leagueId, teamId } = this.state;
    fetch("http://localhost:8080/footballer/espn?leagueID="+leagueId+"&teamID="+teamId)
      .then(res => res.json())
      .then(
        (result) => {
          this.setState({
            isLoaded: true,
            items: result.data.items,
          });
        },
        // Note: it's important to handle errors here
        // instead of a catch() block so that we don't swallow
        // exceptions from actual bugs in components.
        (error) => {
          this.setState({
            isLoaded: true,
            error
          });
        }
      )
  }
  render() {
    const { error, isLoaded, items, leagueName } = this.state;
    if (error) {
      return <div>Error: {error.message}</div>;
    } else if (!isLoaded) {
      return <div>Loading...</div>;
    } else {
      return <div>
        <h2>{leagueName}</h2> 
        {items.map(playerTrade => 
          <div>
            <h3 class="position-header">{playerTrade.position}</h3>
            <div class="player-container">
              <div class="player-fixed">
                <p class="player-trade-header">Possible drops</p>
                <table>
                  <tbody>
                    {getPlayerHeader()}
                    {getPlayers(playerTrade.possiblePlayersToDrop)}
                    
                  </tbody>
                </table> 
              </div>
              <div class="player-item">
                <p class="player-trade-header"> Possible Pickups </p>
                <table>
                  <tbody>
                    {getPlayerHeader()}
                    {getPlayers(playerTrade.possiblePlayersToPickUp)}
                  </tbody>
                </table> 
              </div>
            </div>
          </div>
        )}
      </div> 
    }
  }
}

function getPlayers(players){
  return players.map(player => <Player player={player}/>)
}

function getPlayerHeader(){
  return <Player header={true}/>
}

export default Trade;
