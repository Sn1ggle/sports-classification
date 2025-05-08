function checkFiles(files) {
    if (files.length === 0) {
        alert("Bitte ein Bild auswählen.");
        return;
    }

    const file = files[0];

    // Vorschau anzeigen
    const preview = document.getElementById('preview');
    preview.src = URL.createObjectURL(file);

    // Daten vorbereiten
    const formData = new FormData();
    formData.append("image", file);

    // Anfrage an REST-Service senden
    fetch('/analyze', {
        method: 'POST',
        body: formData
    })
    .then(response => response.text())
    .then(text => {
        document.getElementById('answer').innerText = text;
        document.getElementById('answerPart').style.visibility = "visible";
    })
    .catch(error => {
        console.error('Fehler:', error);
        alert("Fehler beim Senden der Anfrage.");
    });
}
