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
  Button,
   Image,
    Navigator, TouchableHighlight
} from 'react-native';

import EventListPage from './js/EventListPage.js'
import EventDetailPage from './js/EventDetailPage.js'


export default class AweProject extends Component {

constructor(props) {
    super(props);
    this.routes=[{title: 'list', index: 0},{title: 'detail', index: 1}];
    }

 render() {
    return (
      <Navigator
        initialRoute={this.routes[0]}

        renderScene={this.renderNav}

         configureScene={(route, routeStack) =>
           Navigator.SceneConfigs.PushFromRight}

           initialRouteStack={this.routes}

      />
    );
  }

renderNav=(route,nav) => {
        switch (route.index) {
          case 0:
            return <EventListPage navigator={nav} title="list" style={styles.container} />
            case 1:
                 return <EventDetailPage navigator={nav} title="detail" style={styles.container} />
            default:
                return null;
        }
    }

}
AppRegistry.registerComponent('AweProject', () => AweProject);

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'flex-start',
    alignItems: 'center',
    backgroundColor: '#ffffff',
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