const FHIR_URL =
  'http://ec2-54-244-43-45.us-west-2.compute.amazonaws.com:9001/dstu2';

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

    this.state = { active: 0, patients: [], medications: [] };
  }

  render() {
    const results = this.state.patients.map((patient, i) => {
      const { givenName, familyName, gender, birth, city, state, postalCode } = patient;
      const active = this.state.active === i ? ' active' : '';
      const className = `list-group-item${active}`;

      return (
        <a className={className} key={i} onClick={this.handleClick.bind(this, i)}>
          <h4 className="list-group-item-heading">{givenName} {familyName}</h4>
          <p className="list-group-item-text">{this.getAddress(city, state, postalCode)}</p>
        </a>
      );
    });
    const activePatient = this.state.patients[this.state.active] || {};
    const activeMedications = this.state.medications;
    const { givenName, familyName, gender, birth, city, state, postalCode } = activePatient;
    const { identifier, family, given, birthDate } = this.state.criteria || {};

    const searchCritera = !_.isEmpty(this.state.criteria) ? (
      <div className="well">
        <b>Search Criteria: </b>
        {_.filter([
          identifier ? `Identifier: ${identifier}` : '',
          family ? `Family Name: ${family}` : '',
          given ? `Given Name: ${given}` : '',
          birthDate ? `Birth Date: ${birthDate}` : ''
        ]).join(', ')}
      </div>
    ) : undefined;

    return (
      <div>
        {searchCritera}
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
                Age: {getAge(birth)}<br/>
                Born: {(birth || new Date()).toLocaleString()}<br/>
              </div>
              <address className="pull-right">
                {this.getAddress(city, state, postalCode)}
              </address>
            </div>

            <table className="table">
              <thead>
                <tr>
                  <th>Medication Name</th>
                  <th>Prescribed</th>
                  <th>Prescriber</th>
                  <th>Repeats</th>
                  <th>Quantity</th>
                </tr>
              </thead>
              <tbody>
                {activeMedications.map((medication, i) => {
                  const { dateWritten, repeats, quantity, name, prescriber } = medication;
                  return (
                    <tr key={i}>
                      <td>{name}</td>
                      <td>{dateWritten.toLocaleString()}</td>
                      <td>{prescriber}</td>
                      <td>{repeats}</td>
                      <td>{quantity}</td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    );
  }

  componentDidMount() {
    const identifier = $('input[name=identifier]').val()
    const family = $('input[name=family]').val()
    const given = $('input[name=given]').val()
    const birthDate = $('input[name=birthDate]').val()
    const criteria = _.pickBy({ identifier, family, given, birthDate });

    $.ajax({
      type: 'GET',
      contentType: 'application/json',
      url: `${FHIR_URL}/patient`,
      data: criteria,
      dataType: 'json'
    }).done((data) => {
      const patients = _.map(data.entry, 'resource').map((patient) => {
        const officialName = _.first(patient.name, { use: 'official' });
        const givenName = (officialName.given || []).join(' ');
        const familyName = (officialName.family || []).join(' ');
        const gender = patient.gender;
        const birth = new Date(patient.birthDate);
        const { city, state, postalCode } = patient.address[0];
        const identifier = patient.identifier[0].value;

        return { identifier, givenName, familyName, gender, birth, city, state, postalCode };
      });

      this.setState({ patients, criteria });
      this.getMedications(patients[0].identifier);
    });
  }

  handleClick = (key) => {
    this.setState({ active: key }, () => {
      this.getMedications(this.state.patients[key].identifier);
    });
  }

  getAddress(city, state, postalCode) {
    if (city) {
      return `${city}, ${state}, ${postalCode}`;
    } else {
      return `${state}, ${postalCode}`;
    }
  }

  getMedications = (identifer) => {
    $.ajax({
      type: 'GET',
      contentType: 'application/json',
      url: `${FHIR_URL}/medicationorder?patient=${identifer}`,
      dataType: 'json'
    }).done((data) => {
      this.setState({
        medications: _.sortBy(_.map(data.entry, 'resource').map((medication) => {
          const dateWritten = new Date(medication.dateWritten);
          const repeats = medication.dispenseRequest.numberOfRepeatsAllowed;
          const quantity = medication.dispenseRequest.quantity.value;
          const name = medication.medicationReference.display;
          const prescriber = medication.prescriber.display;
          return { dateWritten, repeats, quantity, name, prescriber };
        }), (medication) => {
          return medication.dateWritten.getTime();
        })
      });
    });
  }
}

ReactDOM.render(<Results/>, document.getElementById('results'));
