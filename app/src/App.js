import React, { Component } from 'react';
import './App.css';
import Player from './Player';

class App extends Component {

  constructor(props) {
    super(props);
    this.state = {
      error: null,
      isLoaded: false,
      items: []
    };
  }

  componentDidMount() {
    fetch("http://localhost:8080/footballer/espn?leagueID=102116&teamID=4")
      .then(res => res.json())
      .then(
        (result) => {
          this.setState({
            isLoaded: true,
            items: result.data.items
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
    const { error, isLoaded, items } = this.state;
    if (error) {
      return <div>Error: {error.message}</div>;
    } else if (!isLoaded) {
      return <div>Loading...</div>;
    } else {
      return <div> 
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

export default App;
