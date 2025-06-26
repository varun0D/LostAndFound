const BASE_URL = "http://localhost:8080/items";
 // adjust if running on a different port

document.addEventListener("DOMContentLoaded", () => {
  fetchItems("lost");
  fetchItems("found");

  const form = document.getElementById("reportForm");
  form.addEventListener("submit", (e) => {
    e.preventDefault();
    submitItem();
  });
});

function fetchItems(type) {
  fetch(`${BASE_URL}/${type}`)
    .then((res) => res.json())
    .then((data) => {
      const container = document.getElementById(`${type}-items`);
      container.innerHTML = "";
      data.forEach((item) => {
        const card = document.createElement("div");
        card.className = "item-card";
       card.innerHTML = `
  <div class="item-image">
    <img src="${item.photo}" alt="Item Image" style="max-width: 100%; max-height: 100%;" />
  </div>
  <div class="item-details">
    <h3>${item.name}</h3>
    <p>${item.description}</p>
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

function submitItem() {
  const form = document.getElementById("reportForm");
  const formData = new FormData();

  const item = {
    type: form["report-type"].value,
    name: form["item-name"].value,
    category: form["category"].value,
    location: form["location"].value,
    date: form["date"].value,
    description: form["description"].value,
    contact: form["contact"].value
  };

  formData.append("item", JSON.stringify(item));
  formData.append("photo", form["photo"].files[0]);

  fetch("http://localhost:8080/items/post", {
    method: "POST",
    body: formData
  })
    .then(res => {
      if (!res.ok) throw new Error("Failed to submit");
      alert("Item submitted!");
      form.reset();
      fetchItems("lost");
      fetchItems("found");
    })
    .catch(err => alert("Error: " + err.message));
}
