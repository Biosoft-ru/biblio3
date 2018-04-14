import React from 'react';
import {be5, NavbarMenuContainer, Document, Be5Components} from 'be5-react';


class Application extends React.Component
{
  render() {
    return (
      <div>
        <Be5Components/>
        <NavbarMenuContainer brand='Biblio3'/>
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