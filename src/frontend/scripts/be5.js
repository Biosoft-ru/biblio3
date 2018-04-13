import ReactDOM from 'react-dom';
import React    from 'react';
import { Provider } from 'react-redux';
import { AppContainer } from 'react-hot-loader'
import {Application, be5init, createBaseStore, rootReducer} from 'be5-react';
import './register';


const store = createBaseStore(rootReducer);
be5init.init(store);

const render = Component => {
  ReactDOM.render(
    <AppContainer>
      <Provider store={store}>
        <Application />
      </Provider>
    </AppContainer>,
    document.getElementById('app'),
  )
};

render(Application);

//Webpack Hot Module Replacement API
if (module.hot) {
  module.hot.accept('./register.js', () => {
    render(Application)
  })
}