(function () {
  var root = document.documentElement;
  var theme = localStorage.getItem('sms-theme') === 'dark' ? 'dark' : 'light';
  var themeButtons = document.querySelectorAll('.theme-toggle, .theme-toggle-login');

  function applyTheme(nextTheme) {
    theme = nextTheme;
    root.setAttribute('data-theme', theme);
    localStorage.setItem('sms-theme', theme);

    themeButtons.forEach(function (button) {
      button.setAttribute('aria-pressed', theme === 'dark' ? 'true' : 'false');
      button.setAttribute('aria-label', theme === 'dark' ? 'Switch to light theme' : 'Switch to dark theme');
    });

    document.querySelectorAll('.theme-label').forEach(function (label) {
      label.textContent = theme === 'dark' ? 'Light mode' : 'Dark mode';
    });
  }

  applyTheme(theme);

  themeButtons.forEach(function (button) {
    button.addEventListener('click', function () {
      applyTheme(theme === 'dark' ? 'light' : 'dark');
    });
  });

  var shell = document.querySelector('.app-shell');
  var sidebar = document.getElementById('sidebar');

  if (shell && sidebar) {
    var overlay = document.getElementById('sidebarOverlay');
    var desktopButton = document.getElementById('desktopMenuBtn');
    var mobileButton = document.getElementById('menuBtn');
    var mobileLayout = window.matchMedia('(max-width: 767.98px)');

    function setExpanded(expanded) {
      if (desktopButton) {
        desktopButton.setAttribute('aria-expanded', expanded ? 'true' : 'false');
      }
      if (mobileButton) {
        mobileButton.setAttribute('aria-expanded', expanded ? 'true' : 'false');
        mobileButton.setAttribute('aria-label', expanded ? 'Close navigation' : 'Open navigation');
      }
    }

    function setSidebarHidden(hidden) {
      sidebar.inert = hidden;
      sidebar.setAttribute('aria-hidden', hidden ? 'true' : 'false');
    }

    function closeMobileSidebar(returnFocus) {
      sidebar.classList.remove('open');
      if (overlay) {
        overlay.classList.remove('open');
        overlay.setAttribute('aria-hidden', 'true');
      }
      setExpanded(false);
      setSidebarHidden(true);
      if (returnFocus && mobileButton) {
        mobileButton.focus();
      }
    }

    function openMobileSidebar() {
      setSidebarHidden(false);
      sidebar.classList.add('open');
      if (overlay) {
        overlay.classList.add('open');
        overlay.setAttribute('aria-hidden', 'true');
      }
      setExpanded(true);
      var firstLink = sidebar.querySelector('.sidebar-nav a');
      if (firstLink) {
        firstLink.focus();
      }
    }

    function updateLayout() {
      var focusInSidebar = sidebar.contains(document.activeElement);
      if (mobileLayout.matches) {
        shell.classList.remove('sidebar-collapsed');
        closeMobileSidebar(false);
        if (focusInSidebar && mobileButton) {
          mobileButton.focus();
        }
      } else {
        sidebar.classList.remove('open');
        if (overlay) {
          overlay.classList.remove('open');
          overlay.setAttribute('aria-hidden', 'true');
        }
        var collapsed = localStorage.getItem('sms-sidebar') === 'collapsed';
        if (collapsed && focusInSidebar && desktopButton) {
          desktopButton.focus();
        }
        shell.classList.toggle('sidebar-collapsed', collapsed);
        setExpanded(!collapsed);
        setSidebarHidden(collapsed);
      }
    }

    if (desktopButton) {
      desktopButton.addEventListener('click', function () {
        var collapsed = !shell.classList.contains('sidebar-collapsed');
        if (collapsed && sidebar.contains(document.activeElement)) {
          desktopButton.focus();
        }
        shell.classList.toggle('sidebar-collapsed', collapsed);
        localStorage.setItem('sms-sidebar', collapsed ? 'collapsed' : 'open');
        setExpanded(!collapsed);
        setSidebarHidden(collapsed);
      });
    }

    if (mobileButton) {
      mobileButton.addEventListener('click', function () {
        if (sidebar.classList.contains('open')) {
          closeMobileSidebar(false);
        } else {
          openMobileSidebar();
        }
      });
    }

    if (overlay) {
      overlay.addEventListener('click', function () {
        closeMobileSidebar(true);
      });
    }

    document.addEventListener('keydown', function (event) {
      if (event.key === 'Escape' && sidebar.classList.contains('open')) {
        closeMobileSidebar(true);
      }
    });

    mobileLayout.addEventListener('change', updateLayout);
    updateLayout();
  }

  document.addEventListener('submit', function (event) {
    var form = event.target;
    if (form.matches('form[data-confirm]') && !window.confirm(form.getAttribute('data-confirm'))) {
      event.preventDefault();
    }
  });
})();
