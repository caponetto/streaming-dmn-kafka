const source = new EventSource("/decisions/stream");
source.onmessage = event => {
  const request = JSON.parse(event.data)
  $('#entries > tbody:first').append(`
    <tr>
      <td>${request.subject}</td>
      <td>${request.kogitodmnmodelname}</td>
      <td>${request.data.Driver.Age}</td>
      <td>${request.data.Driver.Points}</td>
      <td>${request.data.Violation.Type}</td>
      <td>${request.data.Violation["Speed Limit"]}</td>
      <td>${request.data.Violation["Actual Speed"]}</td>
    </tr>
  `);
};