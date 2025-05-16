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

    fetch('/labels')
    .then(response => response.json())
    .then(labels => {
        const list = document.getElementById('labelList');
        labels.forEach(label => {
            const li = document.createElement('li');
            li.textContent = label;
            list.appendChild(li);
        });
    })
    .catch(err => {
        console.error("Fehler beim Laden der Labels:", err);
    });

}
