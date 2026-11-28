# Phase 1 Plan — UI/UX Refresh

## Goal

Make the existing Student Management System easier to understand and use by improving its visual consistency, page layout, navigation, responsive behavior, and accessibility. This phase is limited to the current browser interface; it does not add student-management features or change backend behavior.

## Current UI baseline

The application uses Thymeleaf templates and Bootstrap 5.3.3. The dashboard, student list, login, and student form currently define their own inline styles. The dashboard and student list repeat a fixed-width blue sidebar, the student list uses a dense bordered table, and the form has no shared application navigation. These are the starting points for this refresh.

## Phase 1 scope

### 1. Establish a consistent visual system

- Define reusable CSS custom properties for the core palette, surfaces, text, borders, spacing, focus states, and corner radii.
- Use a restrained, accessible palette with consistent semantic colors for primary actions, success, warnings, errors, and inactive states.
- Keep light mode as the default and add a dark theme using the same design tokens.
- Provide a visible theme control; remember the user's selection across page visits and ensure controls remain usable in both themes.
- Move repeated page styling into shared local stylesheets. Continue using the existing Thymeleaf and Bootstrap approach; do not introduce a separate frontend framework.

### 2. Create a shared application shell

- Consolidate repeated branding and navigation into reusable Thymeleaf layout/fragments for the authenticated pages.
- Make the sidebar responsive: persistent navigation on wider screens and an accessible collapsible or off-canvas menu on narrow screens.
- Clearly indicate the current page and keep Dashboard, Students, Add Student, and Logout easy to locate.
- Keep the login page focused and visually consistent without showing authenticated navigation.

### 3. Improve page hierarchy and data presentation

- **Dashboard:** refine spacing and responsive card grids; make key totals easy to scan and preserve the existing counts.
- **Student list:** improve heading and action hierarchy, search placement, table readability, row spacing, and pagination states. Keep existing columns and student actions.
- **Student form:** group related fields, use consistent labels and validation feedback, clarify primary and secondary actions, and make the form comfortable to use on mobile.
- Use consistent button sizes, status treatments, empty/error feedback, and hover/focus states across pages.

### 4. Improve accessibility and usability

- Ensure layouts work at phone, tablet, and desktop widths without horizontal page overflow.
- Use semantic headings, visible keyboard focus, properly associated form labels, and accessible names for interactive controls.
- Maintain readable text and sufficient color contrast in both themes; do not rely on color alone to communicate status or errors.
- Keep actions and feedback understandable without requiring hover or precise pointer interaction.

## Recommended implementation order

1. Define and review the palette, typography, spacing, and light/dark theme tokens.
2. Add shared styles and reusable Thymeleaf layout/navigation fragments.
3. Apply the shared shell and responsive behavior to the dashboard, student list, and student form; keep login visually aligned.
4. Refine the dashboard cards, student table/search/pagination, and form layout.
5. Add the theme control and preference persistence.
6. Verify responsive layouts, keyboard operation, contrast, and existing page workflows; adjust based on findings.

## Acceptance criteria

- All current pages have a coherent visual language; authenticated pages share the same navigation and layout conventions.
- The sidebar/navigation remains usable on desktop and narrow mobile screens, and the active page is apparent.
- Dashboard cards, student table, pagination, and form controls reflow at small viewport widths without page-level horizontal scrolling.
- Light and dark themes cover all current pages, retain readable contrast, and the selected theme persists after navigating or reloading.
- Main workflows remain available: sign in, view dashboard, search/browse students, open add/edit forms, submit a form, and log out.
- Keyboard users can reach and operate navigation, theme selection, search, pagination, and form actions with visible focus.
- Existing server routes, submitted form fields, student columns, and business behavior are preserved.

## Out of scope

- New student, academic, reporting, or account features.
- Backend, database, API, authentication, or authorization changes.
- Changing student search, pagination, or CRUD semantics beyond presentation.
- Replacing Thymeleaf/Bootstrap or introducing a frontend build system or new UI dependency.
- Additional languages, branding redesign, animations, or a full design system for future modules.

## Validation

- Review each page at representative mobile, tablet, and desktop widths.
- Test both themes and verify the preference survives navigation and reload.
- Complete the existing workflows in the acceptance criteria using mouse and keyboard.
- Check labels, focus visibility, error presentation, and text/control contrast in both themes.
- Run the existing Maven test suite and build after implementation; add focused browser-level checks if the project gains a suitable browser test setup.
