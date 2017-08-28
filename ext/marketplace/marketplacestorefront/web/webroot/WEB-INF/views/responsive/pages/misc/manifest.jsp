<%@ page language="java" contentType="application/json" %>
{
"name": "iZooto Push Notification",
"short_name": "iZooto Notifier",
"icons": [{"src": "icon-192x192.png","sizes": "192x192"}],
"start_url": "./index.html?homescreen=1",
"display": "standalone",
"gcm_sender_id": "${gcm_sender_id}"
}