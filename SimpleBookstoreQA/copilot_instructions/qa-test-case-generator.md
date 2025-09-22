# QA Test Case Generator for Simple Digital Bookstore

## Available Test Data Books
- **The Great Gatsby** by F. Scott Fitzgerald - $12.99 - Stock: 9 available
- **To Kill a Mockingbird** by Harper Lee - $14.99 - Stock: 7 available  
- **1984** by George Orwell - $13.99 - Stock: 15 available
- **Pride and Prejudice** by Jane Austen - $11.99 - Stock: 12 available
- **The Catcher in the Rye** by J.D. Salinger - $13.49 - Stock: 6 available

---

## Epic 1: Book Management

### US001: View Available Books Test Cases

#### TC001-001: Display Book List on Homepage
- **Priority**: High
- **Test Type**: Functional
- **Preconditions**: Navigate to bookstore homepage
- **Steps**:
  1. Open the bookstore application
  2. Verify the homepage loads successfully
- **Expected Results**:
  - All 5 books are displayed
  - Each book shows: title, author, price, availability status
  - Book cover images are displayed (if available)
  - Books are properly formatted and readable

#### TC001-002: Verify Book Information Accuracy
- **Priority**: High
- **Test Type**: Data Validation
- **Preconditions**: Homepage is loaded
- **Steps**:
  1. Verify "The Great Gatsby" displays correct information
  2. Verify "To Kill a Mockingbird" displays correct information
  3. Verify "1984" displays correct information
  4. Verify "Pride and Prejudice" displays correct information
  5. Verify "The Catcher in the Rye" displays correct information
- **Expected Results**:
  - All book titles, authors, and prices match expected data
  - Stock availability is correctly displayed for each book
  - Information is clearly visible and properly formatted

#### TC001-003: Pagination Functionality (Future Enhancement)
- **Priority**: Medium
- **Test Type**: Functional
- **Preconditions**: More than 10 books in catalog
- **Steps**:
  1. Navigate to homepage
  2. Check for pagination controls
  3. Navigate between pages if available
- **Expected Results**:
  - Pagination controls appear when needed
  - Page navigation works correctly
  - Maximum books per page is respected

### US002: Search Books Test Cases

#### TC002-001: Search Books by Title
- **Priority**: High
- **Test Type**: Functional
- **Preconditions**: Homepage is loaded with search functionality
- **Steps**:
  1. Enter "gatsby" in search field
  2. Click search button or press Enter
  3. Verify search results
- **Expected Results**:
  - "The Great Gatsby" appears in search results
  - Other books are filtered out
  - Search is case-insensitive

#### TC002-002: Search Books by Author
- **Priority**: High
- **Test Type**: Functional
- **Preconditions**: Homepage is loaded with search functionality
- **Steps**:
  1. Enter "Harper Lee" in search field
  2. Execute search
  3. Verify results
- **Expected Results**:
  - "To Kill a Mockingbird" appears in results
  - Author information is highlighted or clearly visible
  - Only books by Harper Lee are shown

#### TC002-003: Partial Search Functionality
- **Priority**: Medium
- **Test Type**: Functional
- **Preconditions**: Homepage is loaded
- **Steps**:
  1. Enter "Pride" in search field
  2. Execute search
- **Expected Results**:
  - "Pride and Prejudice" appears in results
  - Partial matches are supported

#### TC002-004: Search with No Results
- **Priority**: Medium
- **Test Type**: Negative Testing
- **Preconditions**: Homepage is loaded
- **Steps**:
  1. Enter "NonExistentBook" in search field
  2. Execute search
- **Expected Results**:
  - "No results found" message is displayed
  - Search field remains populated
  - User can easily start a new search

#### TC002-005: Empty Search
- **Priority**: Low
- **Test Type**: Edge Case
- **Preconditions**: Homepage is loaded
- **Steps**:
  1. Leave search field empty
  2. Execute search
- **Expected Results**:
  - All books are displayed OR
  - Appropriate message indicating search field is required

### US003: View Book Details Test Cases

#### TC003-001: View Detailed Book Information
- **Priority**: High
- **Test Type**: Functional
- **Preconditions**: Homepage is loaded
- **Steps**:
  1. Click on "1984" book
  2. Verify book details page loads
- **Expected Results**:
  - Book details page displays
  - Shows book description: "A dystopian social science fiction novel"
  - Displays ISBN number (if available)
  - Shows publication date (if available)
  - Current stock "15 available" is visible

#### TC003-002: View Customer Reviews Section
- **Priority**: Medium
- **Test Type**: Functional
- **Preconditions**: On book details page
- **Steps**:
  1. Navigate to book details page
  2. Scroll to reviews section
- **Expected Results**:
  - Customer reviews section is present
  - Average rating is displayed (if available)
  - Individual review comments are shown (if available)

#### TC003-003: Navigate Back to Book List
- **Priority**: Medium
- **Test Type**: Navigation
- **Preconditions**: On book details page
- **Steps**:
  1. Click back button or navigation link
  2. Verify return to homepage
- **Expected Results**:
  - Successfully returns to book list
  - Previous page state is maintained

---

## Epic 2: Shopping Cart Management

### US004: Add Books to Cart Test Cases

#### TC004-001: Add Single Book to Empty Cart
- **Priority**: High
- **Test Type**: Functional
- **Preconditions**: Cart is empty, on homepage
- **Steps**:
  1. Verify cart shows "0" items
  2. Click "Add to Cart" for "The Great Gatsby"
  3. Verify cart update
- **Expected Results**:
  - Cart count shows "1"
  - Success message appears
  - Book is added to cart
  - Stock count decreases to "8 available"

#### TC004-002: Add Same Book Multiple Times
- **Priority**: High
- **Test Type**: Functional
- **Preconditions**: Cart has one "The Great Gatsby"
- **Steps**:
  1. Click "Add to Cart" for "The Great Gatsby" again
  2. Verify cart and quantity update
- **Expected Results**:
  - Cart count shows "2" OR quantity increases to 2
  - Stock decreases to "7 available"
  - Total cart value updates correctly

#### TC004-003: Add Different Books to Cart
- **Priority**: High
- **Test Type**: Functional
- **Preconditions**: Cart has one book
- **Steps**:
  1. Add "To Kill a Mockingbird" to cart
  2. Verify cart contents
- **Expected Results**:
  - Cart shows 2 different books
  - Total cart value is $12.99 + $14.99 = $27.98
  - Both books appear in cart

#### TC004-004: Add Book with Low Stock
- **Priority**: Medium
- **Test Type**: Edge Case
- **Preconditions**: Cart is empty
- **Steps**:
  1. Add "The Catcher in the Rye" (6 available) to cart
  2. Verify stock update
- **Expected Results**:
  - Book is added successfully
  - Stock shows "5 available"
  - Cart functions normally

#### TC004-005: Attempt to Add Out of Stock Book
- **Priority**: Medium
- **Test Type**: Negative Testing
- **Preconditions**: Book is out of stock (manual setup)
- **Steps**:
  1. Try to add out of stock book
- **Expected Results**:
  - "Add to Cart" button is disabled OR
  - Error message "Out of stock" appears
  - Cart remains unchanged

### US005: View Shopping Cart Test Cases

#### TC005-001: View Cart with Multiple Items
- **Priority**: High
- **Test Type**: Functional
- **Preconditions**: Cart has 3 different books
- **Steps**:
  1. Click on cart icon/link
  2. Verify cart page displays
- **Expected Results**:
  - All 3 books are listed with titles, prices, quantities
  - Individual prices are correct
  - Total cart value is calculated correctly
  - Cart summary is clearly visible

#### TC005-002: Update Quantity from Cart View
- **Priority**: High
- **Test Type**: Functional
- **Preconditions**: Cart has items with quantity controls
- **Steps**:
  1. Change quantity of "1984" from 1 to 3
  2. Update cart
- **Expected Results**:
  - Quantity updates to 3
  - Total price recalculates automatically
  - Stock availability updates accordingly

#### TC005-003: View Empty Cart
- **Priority**: Medium
- **Test Type**: Functional
- **Preconditions**: Cart is empty
- **Steps**:
  1. Navigate to cart page
- **Expected Results**:
  - "Your cart is empty" message is displayed
  - Total shows $0.00
  - Link/button to continue shopping is present

### US006: Remove Items from Cart Test Cases

#### TC006-001: Remove Single Item from Cart
- **Priority**: High
- **Test Type**: Functional
- **Preconditions**: Cart has 3 books
- **Steps**:
  1. Click "Remove" for "Pride and Prejudice"
  2. Verify removal
- **Expected Results**:
  - Book is removed from cart
  - Cart shows 2 books remaining
  - Total price updates (subtracts $11.99)
  - Stock for "Pride and Prejudice" increases back

#### TC006-002: Remove Item with Confirmation
- **Priority**: Medium
- **Test Type**: Functional
- **Preconditions**: Cart has items
- **Steps**:
  1. Click "Remove" for a book
  2. Confirm removal in dialog (if present)
- **Expected Results**:
  - Confirmation dialog appears
  - Item is removed after confirmation
  - Cart updates correctly

#### TC006-003: Remove All Items from Cart
- **Priority**: Medium
- **Test Type**: Functional
- **Preconditions**: Cart has multiple items
- **Steps**:
  1. Remove all items one by one
  2. Verify final cart state
- **Expected Results**:
  - Cart becomes empty
  - "Empty cart" message appears
  - Total shows $0.00
  - All stock quantities are restored

---

## Epic 3: Order Processing

### US007: Place Order Test Cases

#### TC007-001: Complete Order with Valid Information
- **Priority**: High
- **Test Type**: End-to-End
- **Preconditions**: Cart has books ready for checkout
- **Steps**:
  1. Navigate to cart and click "Proceed to Checkout"
  2. Fill in customer information:
     - Name: "John Doe"
     - Email: "john@example.com"
     - Address: "123 Main St, City, State"
  3. Click "Place Order"
- **Expected Results**:
  - Order confirmation page appears
  - Order confirmation number is generated
  - Cart is cleared
  - Stock quantities are permanently reduced

#### TC007-002: Order Validation with Missing Information
- **Priority**: High
- **Test Type**: Negative Testing
- **Preconditions**: Cart has items, on checkout page
- **Steps**:
  1. Leave name field empty
  2. Attempt to place order
- **Expected Results**:
  - Validation error message appears
  - Order is not processed
  - User remains on checkout page
  - Cart contents are preserved

#### TC007-003: Order Confirmation Details
- **Priority**: Medium
- **Test Type**: Functional
- **Preconditions**: Successfully placed an order
- **Steps**:
  1. Review order confirmation page
- **Expected Results**:
  - Shows order details with books purchased
  - Displays total amount paid
  - Provides estimated delivery date
  - Shows customer information used

### US008: View Order History Test Cases

#### TC008-001: View List of Previous Orders
- **Priority**: Medium
- **Test Type**: Functional
- **Preconditions**: Customer has placed orders previously
- **Steps**:
  1. Navigate to "Order History" section
  2. Review order list
- **Expected Results**:
  - List of previous orders with dates
  - Order status for each order
  - Total amount for each order
  - Orders are sorted by date (newest first)

#### TC008-002: View Specific Order Details
- **Priority**: Medium
- **Test Type**: Functional
- **Preconditions**: Order history is available
- **Steps**:
  1. Click on a specific order
  2. View detailed order information
- **Expected Results**:
  - Detailed order information displays
  - Shows all books purchased in that order
  - Displays delivery address
  - Shows order status progression

---

## Epic 4: Admin Functions

### US009: Manage Book Inventory Test Cases

#### TC009-001: View Current Inventory Status
- **Priority**: High
- **Test Type**: Functional
- **Preconditions**: Admin access is available
- **Steps**:
  1. Login as admin
  2. Navigate to inventory management
  3. Review current stock levels
- **Expected Results**:
  - All books show current stock quantities
  - Low stock items are highlighted
  - Inventory data matches frontend display

#### TC009-002: Update Book Stock Quantity
- **Priority**: High
- **Test Type**: Functional
- **Preconditions**: Admin logged in, on inventory page
- **Steps**:
  1. Select "The Great Gatsby"
  2. Update stock from 9 to 15
  3. Save changes
- **Expected Results**:
  - Stock quantity updates to 15
  - Frontend displays updated stock
  - Change is logged/tracked

#### TC009-003: Update Book Information
- **Priority**: Medium
- **Test Type**: Functional
- **Preconditions**: Admin access
- **Steps**:
  1. Edit book details for "1984"
  2. Update price from $13.99 to $15.99
  3. Save changes
- **Expected Results**:
  - Price updates successfully
  - Frontend reflects new price
  - Historical pricing is preserved (if applicable)

### US010: View Sales Reports Test Cases

#### TC010-001: View Total Sales by Date Range
- **Priority**: Medium
- **Test Type**: Functional
- **Preconditions**: Admin access, sales data exists
- **Steps**:
  1. Navigate to sales reports
  2. Select date range (last 30 days)
  3. Generate report
- **Expected Results**:
  - Total sales amount is displayed
  - Number of orders is shown
  - Date range is clearly indicated
  - Data is accurate and up-to-date

#### TC010-002: View Best-Selling Books Report
- **Priority**: Medium
- **Test Type**: Functional
- **Preconditions**: Admin access, purchase history exists
- **Steps**:
  1. Generate best-selling books report
  2. Review top 5 books
- **Expected Results**:
  - Books are ranked by quantity sold
  - Shows units sold for each book
  - Revenue per book is displayed
  - Report covers specified time period

#### TC010-003: Generate Basic Analytics
- **Priority**: Low
- **Test Type**: Functional
- **Preconditions**: Admin access
- **Steps**:
  1. View analytics dashboard
  2. Review key metrics
- **Expected Results**:
  - Average order value is shown
  - Customer acquisition metrics (if available)
  - Inventory turnover rates
  - Visual charts/graphs for easy interpretation

---

## Cross-Functional Test Cases

### Performance Test Cases

#### TC-PERF-001: Homepage Load Time
- **Priority**: Medium
- **Test Type**: Performance
- **Steps**:
  1. Measure page load time for homepage
  2. Verify all books load within acceptable time
- **Expected Results**:
  - Page loads within 3 seconds
  - All book images load progressively

#### TC-PERF-002: Search Response Time
- **Priority**: Medium
- **Test Type**: Performance
- **Steps**:
  1. Perform search operations
  2. Measure response time
- **Expected Results**:
  - Search results appear within 1 second
  - No performance degradation with multiple searches

### Usability Test Cases

#### TC-USAB-001: Navigation Ease
- **Priority**: Medium
- **Test Type**: Usability
- **Steps**:
  1. Navigate through all major sections
  2. Evaluate user experience
- **Expected Results**:
  - Clear navigation paths
  - Consistent UI elements
  - Intuitive user flow

### Security Test Cases

#### TC-SEC-001: Admin Access Control
- **Priority**: High
- **Test Type**: Security
- **Steps**:
  1. Attempt to access admin functions without login
  2. Verify access restrictions
- **Expected Results**:
  - Admin functions are protected
  - Proper authentication required
  - Unauthorized access is prevented

---

## Test Execution Guidelines

### Test Environment Requirements
- Web browser (Chrome, Firefox, Safari)
- Internet connection
- Clean browser state (cleared cache/cookies)
- Admin credentials (for admin test cases)

### Test Data Management
- Use the 5 predefined books for all testing
- Reset cart state between test scenarios
- Maintain consistent stock levels for repeatability
- Document any stock changes during testing

### Reporting
- Document actual vs expected results
- Capture screenshots for visual verification
- Note any deviations from expected behavior
- Track defects with clear reproduction steps

### Automation Considerations
- Prioritize high-priority test cases for automation
- Focus on repetitive test scenarios
- Maintain test data consistency
- Implement proper wait strategies for dynamic content
