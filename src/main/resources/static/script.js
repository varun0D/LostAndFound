const BASE_URL = "http://localhost:8080/items";

document.addEventListener("DOMContentLoaded", () => {
  // Load lost and found items on page load
  fetchItems("lost");
  fetchItems("found");

  // Attach submit event to the report form
  const form = document.getElementById("reportForm");
  form.addEventListener("submit", (e) => {
    e.preventDefault();
    submitItem();
  });
});

/**
 * Fetch items of a given status ("lost" or "found") and display them
 * @param {string} status - "lost" or "found"
 */
function fetchItems(status) {
  fetch(`${BASE_URL}/${status}`)
    .then((res) => res.json())
    .then((data) => {
      const container = document.getElementById(`${status}-items`);
      container.innerHTML = "";

      if (data.length === 0) {
        container.innerHTML = `<p>No ${status} items found.</p>`;
        return;
      }

      data.forEach((item) => {
        const card = document.createElement("div");
        card.className = "item-card";

        card.innerHTML = `
          <div class="item-image">
            <img src="${item.photo}" alt="Item Image" style="max-width: 100%; max-height: 100%;" />
          </div>
          <div class="item-details">
            <h3>${item.title}</h3>
            <p><strong>Description:</strong> ${item.description}</p>
            <p><strong>Category:</strong> ${item.category}</p>
            <p><strong>Location:</strong> ${item.location}</p>
            <p><strong>Contact:</strong> ${item.contact}</p>
            <p class="found-date"><strong>Date:</strong> ${item.date}</p>
          </div>
        `;

        container.appendChild(card);
      });
    })
    .catch((err) => console.error("Error loading items:", err));
}

/**
 * Collect form data, convert item info to JSON, append photo,
 * and send multipart/form-data POST request to backend.
 */
function submitItem() {
  const form = document.getElementById("reportForm");

  // Construct item object matching backend model
  const item = {
    status: form["report-type"].value,         // maps to Item.status
    title: form["item-name"].value,            // maps to Item.title
    category: form["category"].value,
    location: form["location"].value,
    date: form["date"].value,
    description: form["description"].value,
    contact: form["contact"].value,
    photo: ""  // photo handled separately
  };

  // Validate required fields (simple example)
  if (!item.title || !item.status || !item.description) {
    alert("Please fill in all required fields.");
    return;
  }

  // Prepare FormData for multipart request
  const formData = new FormData();
  formData.append("item", JSON.stringify(item));

  // Append photo only if file selected
  const photoFile = form["photo"].files[0];
  if (photoFile) {
    formData.append("photo", photoFile);
  }

  fetch(`${BASE_URL}/post`, {
    method: "POST",
    body: formData,
  })
    .then((res) => {
      if (!res.ok) throw new Error(`Failed to submit: ${res.statusText}`);
      return res.json();
    })
    .then((savedItem) => {
      alert("Item submitted successfully!");
      form.reset();

      // Refresh lists
      fetchItems("lost");
      fetchItems("found");
    })
    .catch((err) => {
      console.error(err);
      alert("Error submitting item: " + err.message);
    });
}
