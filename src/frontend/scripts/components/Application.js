import React from 'react';
import {be5, Be5Menu, Document, Be5Components} from 'be5-react';


class Application extends React.Component
{
  render() {
    return (
      <div>
        <Be5Components/>
        <Be5Menu ref="menu" show={true}/>
        <div className="container">
          <div className="row">
            <Document ref="document" frontendParams={{documentName: be5.MAIN_DOCUMENT}} />
          </div>
        </div>
      </div>
    );

  }

}

export default Application;