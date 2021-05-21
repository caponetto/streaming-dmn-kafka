const source = new EventSource("/decisions/stream");
source.onmessage = event => {
  const response = JSON.parse(event.data)

  if (response.type !== "DecisionResponse") {
    return;
  }

  const decision = response.data["Should the driver be suspended?"];
  const decisionClass = "badge rounded-pill bg-" + (decision === "Yes" ? "danger" : "success");

  $('#entries > tbody:first').append(`
    <tr>
      <td>${response.subject}</td>
      <td>${response.kogitodmnmodelname}</td>
      <td>${response.data.Driver.Age}</td>
      <td>${response.data.Driver.Points}</td>
      <td>${response.data.Violation.Type}</td>
      <td>${response.data.Violation["Speed Limit"]}</td>
      <td>${response.data.Violation["Actual Speed"]}</td>
      <td>${response.data.Fine.Points}</td>
      <td>${response.data.Fine.Amount}</td>
      <td><span class="${decisionClass}">${decision}</span></td>
    </tr>
  `);
};