const canvas = document.getElementById('scratchCanvas');
const ctx = canvas.getContext('2d');
const hiddenImage = document.getElementById('hiddenImage');

// Adjust canvas size
canvas.width = 300;
canvas.height = 300;

// Fill canvas with a solid color (the "scratch" layer)
ctx.fillStyle = "#bbb";
ctx.fillRect(0, 0, canvas.width, canvas.height);

// To store the mouse movement
let isDrawing = false;

function revealScratch(event) {
  if (!isDrawing) return;

  const rect = canvas.getBoundingClientRect();
  const x = event.clientX - rect.left;
  const y = event.clientY - rect.top;

  // Clear a circle at the current position to simulate scratching
  ctx.globalCompositeOperation = 'destination-out';
  ctx.beginPath();
  ctx.arc(x, y, 15, 0, 2 * Math.PI);
  ctx.fill();
}

canvas.addEventListener('mousedown', () => {
  isDrawing = true;
});

canvas.addEventListener('mouseup', () => {
  isDrawing = false;
});

canvas.addEventListener('mousemove', revealScratch);

// Function to reveal the image once 50% of the area is scratched
function checkScratchCompletion() {
  const imageData = ctx.getImageData(0, 0, canvas.width, canvas.height);
  let transparentPixels = 0;
  const totalPixels = imageData.data.length / 4;

  for (let i = 0; i < totalPixels; i++) {
    if (imageData.data[i * 4 + 3] === 0) { // Check alpha channel for transparency
      transparentPixels++;
    }
  }

  const scratchPercentage = transparentPixels / totalPixels;

  if (scratchPercentage > 0.5) {
    hiddenImage.style.display = 'block'; // Reveal the hidden image
  }

  requestAnimationFrame(checkScratchCompletion);
}

// Start checking the scratch completion
checkScratchCompletion();
