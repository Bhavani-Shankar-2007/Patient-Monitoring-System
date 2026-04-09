let currentPatientId = null;

async function loadPatients() {
    try {
        const res = await fetch('http://localhost:8080/api/patients');
        if (!res.ok) throw new Error("Failed to load patients");
        
        const patients = await res.json();
        const container = document.getElementById('patientList');
        container.innerHTML = `<div class="p-3"><h5 class="mb-3 text-warning"><i class="fas fa-users"></i> Patients</h5></div>`;

        patients.forEach(p => {
            const div = document.createElement('div');
            div.className = `card mx-3 mb-3 p-3 cursor-pointer ${currentPatientId === p.id ? 'border-primary' : ''}`;
            div.innerHTML = `
                <h6 class="text-white">${p.name}</h6>
                <small class="text-muted">${p.condition} • Age ${p.age}</small><br>
                <div class="progress mt-2" style="height: 10px;">
                    <div class="progress-bar" style="width: ${p.medicationAdherence}%"></div>
                </div>
                <small class="text-white-50">Medication: ${p.medicationAdherence}%</small>
            `;
            div.onclick = () => selectPatient(p);
            container.appendChild(div);
        });
    } catch (e) {
        document.getElementById('patientList').innerHTML = `
            <div class="p-4 text-center text-danger">
                <i class="fas fa-exclamation-triangle"></i><br>
                Cannot connect to backend.<br>
                Make sure Spring Boot is running on port 8080.
            </div>`;
    }
}

async function selectPatient(patient) {
    currentPatientId = patient.id;
    loadPatients(); // refresh highlight

    const detail = document.getElementById('patientDetail');
    document.getElementById('patientName').textContent = patient.name;

    detail.innerHTML = `
        <div class="row text-center mb-4">
            <div class="col">
                <strong class="text-warning">Medication</strong><br>
                <span class="fs-1 fw-bold text-danger">${patient.medicationAdherence}%</span>
            </div>
            <div class="col">
                <strong class="text-warning">Appointments</strong><br>
                <span class="fs-1 fw-bold">${patient.appointmentsAttended}/${patient.totalAppointments}</span>
            </div>
            <div class="col">
                <strong class="text-warning">Lifestyle</strong><br>
                <span class="fs-1 fw-bold text-success">${patient.lifestyleScore}%</span>
            </div>
        </div>
    `;

    // Log Form
    document.getElementById('logForm').innerHTML = `
        <form id="logFormData">
            <div class="mb-3">
                <label class="form-label">Medication Adherence Today (%)</label>
                <input type="number" id="medAdh" class="form-control" value="90" min="0" max="100">
            </div>
            <div class="row">
                <div class="col">
                    <label class="form-label">Appointments Attended</label>
                    <input type="number" id="att" class="form-control" value="1" min="0">
                </div>
                <div class="col">
                    <label class="form-label">Total Scheduled</label>
                    <input type="number" id="tot" class="form-control" value="1" min="1">
                </div>
            </div>
            <div class="mb-3 mt-3">
                <label class="form-label">Lifestyle Score (0-100)</label>
                <input type="number" id="life" class="form-control" value="85" min="0" max="100">
            </div>
            <div class="mb-3">
                <label class="form-label">Quick Log Note</label>
                <input type="text" id="logNote" class="form-control" placeholder="e.g. Took medicine on time 🕸️">
            </div>
            <button type="button" onclick="submitLog()" class="btn btn-danger w-100">
                <i class="fas fa-save"></i> Save Log
            </button>
        </form>
    `;
}

async function submitLog() {
    if (!currentPatientId) return alert("🕷️ Select a patient first!");

    const data = {
        medicationAdherence: document.getElementById('medAdh').value,
        appointmentsAttended: document.getElementById('att').value,
        totalAppointments: document.getElementById('tot').value,
        lifestyleScore: document.getElementById('life').value,
        logEntry: document.getElementById('logNote').value || "No note"
    };

    try {
        await fetch(`http://localhost:8080/api/patients/${currentPatientId}/log`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        alert('✅ Data logged successfully! Web slinging complete!');
        loadPatients();
    } catch (e) {
        alert('❌ Failed to save log. Is backend running?');
    }
}

async function generateIntervention() {
    if (!currentPatientId) return alert("🕷️ Select a patient first!");

    try {
        const res = await fetch(`http://localhost:8080/api/patients/${currentPatientId}/intervention`, { 
            method: 'POST' 
        });

        if (!res.ok) throw new Error();

        const intervention = await res.json();

        const html = `
            <div class="alert alert-success">
                <h6><i class="fas fa-magic"></i> AI Intervention Strategies</h6>
                <h6 class="mt-3">🔔 Reminders</h6>
                <ul>${intervention.reminders.map(r => `<li>${r}</li>`).join('')}</ul>
                
                <h6 class="mt-3">📚 Educational Content</h6>
                <ul>${intervention.educationalContent.map(e => `<li>${e}</li>`).join('')}</ul>
                
                <h6 class="mt-3">📢 Care Team Notifications</h6>
                <ul>${intervention.careTeamNotifications.map(n => `<li>${n}</li>`).join('')}</ul>
            </div>
        `;
        document.getElementById('interventionResult').innerHTML = html;
    } catch (e) {
        document.getElementById('interventionResult').innerHTML = `
            <div class="alert alert-danger">
                Failed to generate intervention.<br>
                Check if Gemini API key is correct in application.properties
            </div>`;
    }
}

// Initial load
window.onload = loadPatients;