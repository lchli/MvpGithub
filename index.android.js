/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */
'use strict';

import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View,
  ListView,
  Button
} from 'react-native';


export default class AweProject extends Component {



constructor(props) {
    super(props);
   const ds = new ListView.DataSource({rowHasChanged: (r1, r2) => r1 !== r2});

      this.state = {
      dataSource: ds.cloneWithRows(['445']),
      dss:ds


      };



  }

//get data source.
 getDataSource2(movies) {
        return this.state.dss.cloneWithRows(movies);
    }


onSearchChange() {

  fetch('https://facebook.github.io/react-native/movies.json')
      .then((response) => response.json())
      .then((responseJson) => {

//var resultData = [];
//            for (var i = 0, len = responseJson.movies.length; i < len; i++) {
//                resultData.push(responseJson.movies[i]);
//            }

  this.setState({

                 dataSource: this.getDataSource2(responseJson.movies)
             });


        return responseJson.movies;
      })
      .catch((error) => {
        console.error(error);
      });
  }






  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>
          Welcome to React Native!00000
        </Text>
        <Text style={styles.instructions}>
          To get started, edit index.android.js
        </Text>
        <Text style={styles.instructions}>
          Double tap R on your keyboard to reload,{'\n'}
          Shake or press menu button for dev menu
        </Text>

       <Button
         onPress={this.onSearchChange.bind(this)}
         title="Learn More"
         color="#841584"
         accessibilityLabel="Learn more about this purple button"
       />

        <ListView
                dataSource={this.state.dataSource}
                renderRow={(rowData) => <Text>{rowData.title}</Text>}
              />
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'flex-start',
    alignItems: 'center',
    backgroundColor: '#abcdef',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});

AppRegistry.registerComponent('AweProject', () => AweProject);
