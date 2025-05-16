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
window.onload = function () {
    fetch('/labels')
        .then(response => response.json())
        .then(labels => {
            const grid = document.getElementById('labelGrid');
            const columns = 4;
            const itemsPerCol = Math.ceil(labels.length / columns);

            for (let i = 0; i < columns; i++) {
                const col = document.createElement('div');
                col.className = 'col-sm-3';
                const ul = document.createElement('ul');
                ul.classList.add('list-unstyled');

                for (let j = 0; j < itemsPerCol; j++) {
                    const index = i * itemsPerCol + j;
                    if (index < labels.length) {
                        const li = document.createElement('li');
                        li.textContent = labels[index];
                        ul.appendChild(li);
                    }
                }

                col.appendChild(ul);
                grid.appendChild(col);
            }
        })
        .catch(err => {
            console.error("Fehler beim Laden der Labels:", err);
        });
};

