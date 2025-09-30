# 📝 Copilot Agent Instructions — Controlled DOM ID & Selector Refactor

## 🎯 Goal
We are refactoring only **6–7 DOM IDs and critical selectors** that Healenium tracks.  
The bookstore app functionality, UI, and usage must remain **unchanged**.  
This change is for a **demo branch** to showcase Healenium healing.  

---

## 1. Branch Setup
\`\`\`bash
git checkout -b feature/healenium-demo
\`\`\`
All refactoring must be done in this branch only.

The main branch remains the stable baseline.

---

## 2. Scope of Changes
✅ Only change the following:

- By.id(...) selectors (7 of them)  
- By.xpath(//*[@id=...]) selectors tied to Admin Page forms  
- By.cssSelector selectors directly tied to page navigation or forms  

❌ Do not change:

- Class-based selectors (.book-card, .btn-primary, etc.)  
- Styling classes that define layout, fonts, or colors  
- Backend code (.java files)  

---

## 3. ID & Selector Mapping (Old → New)
\`\`\`json
{
  "cart-items": "cart-item-list",
  "admin-page": "admin-dashboard",
  "books-grid": "book-list-view",
  "add-book-form": "book-entry-panel",
  "home-page": "homepage-container",
  "cart-page": "shopping-cart-page",
  "search-input": "book-search-box",
  "book-title": "new-book-title",
  "book-author": "new-book-author",
  "book-description": "new-book-desc",
  "book-isbn": "new-book-isbn",
  "book-price": "new-book-price",
  "book-stock": "new-book-stock"
}
\`\`\`
👉 Out of this, apply 6–7 changes only (e.g., the first 7).  
The rest can stay as fallback if you want to extend later.

---

## 4. Update Files
- **index.html**  
  Replace old IDs with new ones (only from mapping above).  
  Ensure element structure is unchanged.  

- **bookstore.js**  
  Update all DOM references:  
  - getElementById("...")  
  - querySelector("#...")  
  Use new IDs from mapping.  

- **style.css**  
  Replace #old-id { ... } with #new-id { ... }.  
  Do not touch class selectors.  

---

## 5. Verify Functionality
Run app:
\`\`\`bash
mvn spring-boot:run
\`\`\`
Test in browser:

- Add book (admin page) still works  
- Search still works  
- Cart still works  
- Confirm UI is identical  

---

## 6. Commit Changes
\`\`\`bash
git add src/main/resources/static/index.html         src/main/resources/static/js/bookstore.js         src/main/resources/static/css/style.css
git commit -m "Refactor 7 DOM IDs for Healenium demo (HTML, JS, CSS updated)"
\`\`\`

---

## 7. Demo Flow
- On main branch → tests run fine with old locators.  
- On feature/healenium-demo branch → tests initially break → Healenium heals selectors → tests pass.  

---

## ✅ Outcome
- Controlled changes (6–7 IDs only)  
- No change in looks, usage, or functionality  
- Perfect scenario for Healenium healing demo  
