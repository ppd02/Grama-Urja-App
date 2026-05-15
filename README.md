<h1 align="center">Grama Urja App</h1>

<h3 align="center">
Crowdsourced Rural Power Monitoring & Smart Irrigation Assistant
</h3>

<p align="center">
An Android application that helps rural farmers monitor real-time electricity availability and improve irrigation planning using Firebase and Generative AI.
</p>

<p align="center">
<!-- <img src="https://github.com/user-attachments/assets/74907430-c3b3-425e-bc57-f9389bd5dc5a" width="100%"/> -->
</p>

<hr>

<h2>📌 Overview</h2>

<p>
Grama-Urja is a smart rural Android application designed to help farmers know whether electricity is available in their transformer zone without physically visiting their fields.
</p>

<p>
Farmers can collaboratively update power status as <b>ON</b> or <b>OFF</b>, and all users in the same zone receive live updates instantly. The app also includes irrigation assistance, push notifications, and an AI-powered farming assistant.
</p>

<hr>

<h2>🎯 Problem Statement</h2>

<ul>
<li>Rural electricity supply is irregular and unpredictable.</li>
<li>Farmers travel long distances to check if power has returned.</li>
<li>This causes time loss, physical effort, and delayed irrigation.</li>
<li>Water and energy resources are often used inefficiently.</li>
<li>There is no simple community-driven real-time monitoring solution.</li>
</ul>

<hr>

<h2>🚀 Features</h2>

<ul>
<li>⚡ Real-time Power Status Monitoring</li>
<li>📡 Firebase Realtime Synchronization</li>
<li>🔔 Push Notifications using Firebase Cloud Messaging</li>
<li>👨‍🌾 Transformer Zone-based Updates</li>
<li>📊 Community Reports Dashboard</li>
<li>💧 Irrigation Pump Timer Calculator</li>
<li>🤖 AI Farming Assistant using Gemini/OpenAI</li>
<li>🕒 Timestamp Freshness Tracking</li>
<li>📱 High Contrast Rural-Friendly UI</li>
</ul>

<hr>

<h2>🛠 Tech Stack</h2>

<table>
<tr>
<th>Technology</th>
<th>Purpose</th>
</tr>

<tr>
<td>Kotlin</td>
<td>Android Application Development</td>
</tr>

<tr>
<td>XML</td>
<td>User Interface Design</td>
</tr>

<tr>
<td>Firebase Realtime Database</td>
<td>Real-time Data Synchronization</td>
</tr>

<tr>
<td>Firebase Authentication</td>
<td>User Login & Registration</td>
</tr>

<tr>
<td>Firebase Cloud Messaging</td>
<td>Push Notifications</td>
</tr>

<tr>
<td>Retrofit</td>
<td>REST API Integration</td>
</tr>

<tr>
<td>Gemini/OpenAI API</td>
<td>Generative AI Assistant</td>
</tr>

<tr>
<td>MVVM Architecture</td>
<td>Clean & Scalable Code Structure</td>
</tr>

</table>

<hr>

<h2>📸 Application Screenshots</h2>

<table>
<tr>
<td align="center">
<h4>Profile</h4>
<img src="https://github.com/user-attachments/assets/46d47f8d-f859-4a6a-a791-ad1cfb695447" width="250"/>
</td>

<td align="center">
<h4>Dashboard</h4>
<img src="https://github.com/user-attachments/assets/bdf9681f-8a99-4daf-862e-09b4edf56f50" width="250"/>
</td>

<td align="center">
<h4>Notifications</h4>
<img src="https://github.com/user-attachments/assets/0f66db1f-9b10-447c-90f3-9875fde235cb" width="250"/>
</td>
</tr>

<tr>
<td align="center">
<h4>Pump Timer</h4>
<img src="https://github.com/user-attachments/assets/b8005609-2649-483f-a1f9-9dd53aa3ca84" width="250"/>
</td>

<td align="center">
<h4>AI Assistant & Market Values</h4>
<img src="https://github.com/user-attachments/assets/360f5eb6-8d08-41c9-9f90-da4c403e8665" width="250"/>
</td>

<td align="center">
<h4>Settings</h4>
<img src="https://github.com/user-attachments/assets/17a55624-2060-4cce-8744-00b24916a79f" width="250"/>
</td>
</tr>
</table>

<hr>

<h2>📱 Application Screens</h2>

<ol>
<li>Live Power Dashboard</li>
<li>Community Reports Screen</li>
<li>Pump Timer Calculator</li>
<li>AI Assistant Screen</li>
<li>Notifications & Alerts Screen</li>
</ol>

<hr>

<h2>🧠 System Workflow</h2>

<pre>
Farmer Updates Power Status
            ↓
Firebase Realtime Database
            ↓
Live Sync to Same Transformer Zone
            ↓
Push Notification Trigger
            ↓
Farmers Receive Instant Alert
            ↓
Irrigation Planning & Pump Usage
</pre>

<hr>

<h2>🔥 Firebase Database Structure</h2>

<pre>
users
   uid_001
      name: Ramesh
      village: Angondhalli
      zone: T1

zones
   T1
      powerStatus: ON
      updatedBy: Ramesh
      timestamp: 17145345345

reports
   T1
      report1
         user: Ramesh
         status: ON
         timestamp: 17145345345
</pre>

<hr>

<h2>⚙️ Firebase Setup</h2>

<h3>Step 1</h3>
<p>Create a Firebase project.</p>

<h3>Step 2</h3>

<pre>
Package Name:
com.gramaurja.app
</pre>

<p>Download:</p>

<pre>
google-services.json
</pre>

<p>Place inside:</p>

<pre>
app/google-services.json
</pre>

<h3>Step 3</h3>

<p>Enable the following Firebase services:</p>

<ul>
<li>Firebase Authentication</li>
<li>Firebase Realtime Database</li>
<li>Firebase Cloud Messaging</li>
</ul>

<hr>

<h2>📦 Dependencies</h2>

<pre>
implementation 'com.google.firebase:firebase-auth-ktx'
implementation 'com.google.firebase:firebase-database-ktx'
implementation 'com.google.firebase:firebase-messaging-ktx'
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
</pre>

<hr>

<h2>📂 Recommended Project Structure</h2>

<pre>
Grama-Urja/
│
├── screenshots/
│   ├── banner.png
│   ├── login_screen.png
│   ├── dashboard_screen.png
│   ├── reports_screen.png
│   ├── pump_timer_screen.png
│   ├── assistant_screen.png
│   └── notification_screen.png
│
├── app/
├── README.md
</pre>

<hr>

<h2>🌱 Future Enhancements</h2>

<ul>
<li>AI-based Power Availability Prediction</li>
<li>Voice Assistant in Regional Languages</li>
<li>GPS Verified Status Reports</li>
<li>Weather-integrated Irrigation Suggestions</li>
<li>Offline Synchronization Support</li>
<li>Transformer Analytics Dashboard</li>
<li>Community Discussion Forum</li>
</ul>

<hr>

<h2>🌍 Real World Impact</h2>

<ul>
<li>⏳ Saves farmers’ time and effort</li>
<li>💧 Improves irrigation efficiency</li>
<li>⚡ Encourages smarter energy usage</li>
<li>🌱 Supports sustainable agriculture</li>
<li>🤝 Promotes community-driven rural intelligence</li>
<li>📲 Provides low-cost digital infrastructure support</li>
</ul>

<hr>

<h2>👨‍💻 Developed Using</h2>

<p>
Android Studio • Kotlin • Firebase • Generative AI
</p>

<hr>

<h2>📜 License</h2>

<p>
This project is developed for educational, innovation, and rural development purposes.
</p>
