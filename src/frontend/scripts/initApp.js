import ReactDOM     from 'react-dom';
import React        from 'react';
import { Provider } from 'react-redux';
import {Application, initBe5App, createBaseStore, rootReducer} from 'be5-react';
import App from './components/Application'
import './register';


const store = createBaseStore(rootReducer);
const render = Component => {
  ReactDOM.render(
    <AppContainer>
      <Provider store={store}>
        <Component />
      </Provider>
    </AppContainer>,
    document.getElementById('app'),
  )
};

initBe5App(store, function () {
  render(App);
});

//Webpack Hot Module Replacement API
if (module.hot) {
  module.hot.accept('./components/Application', () => {
    render(Application)
  })
}
