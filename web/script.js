// script.js - simple calculator logic for web UI
(() => {
  const display = document.getElementById("display");
  let current = "";
  let stored = null;
  let op = null;

  function updateDisplay(v) {
    display.textContent = v === "" ? "0" : v;
  }

  function applyOperation(a, b, o) {
    a = parseFloat(a);
    b = parseFloat(b);
    if (isNaN(a) || isNaN(b)) return b;
    switch (o) {
      case "+": return a + b;
      case "-": return a - b;
      case "*": return a * b;
      case "/": return b === 0 ? "Error" : a / b;
      default: return b;
    }
  }

  document.querySelectorAll(".btn").forEach(btn => {
    btn.addEventListener("click", () => {
      const key = btn.dataset.key;
      if (!key) return;
      if (key === "C") {
        current = "";
        stored = null;
        op = null;
        updateDisplay("");
        return;
      }
      if (key === "±") {
        if (current) {
          if (current.startsWith("-")) current = current.slice(1);
          else current = "-" + current;
        } else if (display.textContent && display.textContent !== "0") {
          current = "-" + display.textContent;
        }
        updateDisplay(current);
        return;
      }
      if (key === "=") {
        if (op && current !== "") {
          const res = applyOperation(stored, current, op);
          current = "" + res;
          stored = null;
          op = null;
          updateDisplay(current);
        }
        return;
      }
      if (["+", "-", "*", "/"].includes(key)) {
        if (current === "" && display.textContent !== "0") {
          // use displayed as stored
          stored = display.textContent;
        } else if (current !== "") {
          if (stored === null) stored = current;
          else stored = "" + applyOperation(stored, current, op || key);
        }
        op = key;
        current = "";
        updateDisplay(stored);
        return;
      }
      // number or dot
      if (key === ".") {
        if (!current.includes(".")) current = current === "" ? "0." : current + ".";
      } else {
        current = (current === "0") ? key : current + key;
      }
      updateDisplay(current);
    });
  });

})();