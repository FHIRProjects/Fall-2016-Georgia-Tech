const FHIR_URL =
  'http://ec2-54-244-43-45.us-west-2.compute.amazonaws.com:9001/dstu2/patient';

function getAge(date) {
    const birthDate = date || new Date();
    const today = new Date();
    let age = today.getFullYear() - birthDate.getFullYear();
    const m = today.getMonth() - birthDate.getMonth();
    if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
        age--;
    }
    return age;
}

class Results extends React.Component {
  constructor(props) {
    super(props);

    this.state = { active: 0, patients: [] };
  }

  render() {
    const results = this.state.patients.map((patient, i) => {
      const { givenName, familyName, gender, birthDate, city, state, postalCode } = patient;
      const active = this.state.active === i ? ' active' : '';
      const className = `list-group-item${active}`;

      return (
        <a className={className} key={i} onClick={this.handleClick.bind(this, i)}>
          <h4 className="list-group-item-heading">{givenName} {familyName}</h4>
          <p className="list-group-item-text">{city}, {state}, {postalCode}</p>
        </a>
      );
    });
    const activePatient = this.state.patients[this.state.active] || {};
    const { givenName, familyName, gender, birthDate, city, state, postalCode } = activePatient;

    return (
      <div>
        <div className="col-md-4">
          <div className="panel panel-default">
            <div className="panel-heading">Results ({this.state.patients.length})</div>
            <div className="list-group">
              {results}
            </div>
          </div>
        </div>
        <div className="col-md-8">
          <div className="panel panel-default">
            <div className="panel-heading"><h4>{givenName} {familyName}</h4></div>
            <div className="panel-body">
              <div className="pull-left">
                Gender: {gender}<br/>
                Age: {getAge(birthDate)}<br/>
                Born: {(birthDate || new Date()).toLocaleString()}<br/>
              </div>
              <address className="pull-right">
                {city}, {state}, {postalCode}
              </address>
            </div>

            <table className="table">
              <thead>
                <tr>
                  <th>Medication Name</th>
                  <th>First Prescribed</th>
                  <th>Repeats</th>
                  <th>Dosage</th>
                  <th>Quantity</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>NDC</td>
                  <td>January 1, 2000</td>
                  <td>0</td>
                  <td>160 mg</td>
                  <td>50 pills</td>
                </tr>
                <tr>
                  <td>NDC</td>
                  <td>January 1, 2000</td>
                  <td>0</td>
                  <td>160 mg</td>
                  <td>50 pills</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    );
  }

  componentDidMount() {
    $.ajax({
      type: 'GET',
      contentType: 'application/json',
      url: FHIR_URL,
      dataType: 'json'
    }).done((data) => {
      this.setState({ patients: _.map(data.entry, 'resource').map((patient) => {
          const officialName = _.first(patient.name, { use: 'official' });
          const givenName = (officialName.given || []).join(' ');
          const familyName = (officialName.family || []).join(' ');
          const gender = patient.gender;
          const birthDate = new Date(patient.birthDate);
          const { city, state, postalCode } = patient.address[0];

          return { givenName, familyName, gender, birthDate, city, state, postalCode };
        })
      });
    });
  }

  handleClick = (key) => {
    this.setState({ active: key });
  }
}

ReactDOM.render(<Results/>, document.getElementById('results'));
