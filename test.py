# MODEL CLASS

class CNN:

    def __init__(self, num_filters=4, hidden_size=128, lr=0.01):
        self.num_filters = num_filters
        self.lr = lr

        # Fixed conv filters (not trained)
        self.filters = [np.random.randn(3, 3) * 0.1 for _ in range(num_filters)]

        flat_size = num_filters * 26 * 26  # 2704

        self.W1 = np.random.randn(hidden_size, flat_size)   * 0.01
        self.b1 = np.zeros(hidden_size)
        self.W2 = np.random.randn(10, hidden_size) * 0.01
        self.b2 = np.zeros(10)

    # ---------- helpers ----------

    def _relu(self, x):
        return np.maximum(0, x)

    def _relu_grad(self, x):
        return (x > 0).astype(float)

    def _softmax(self, x):
        e = np.exp(x - np.max(x))
        return e / e.sum()

    def _convolve(self, image, kernel):
        iH, iW = image.shape
        kH, kW = kernel.shape
        outH, outW = iH - kH + 1, iW - kW + 1
        out = np.zeros((outH, outW))
        for i in range(outH):
            for j in range(outW):
                out[i, j] = np.sum(image[i:i+kH, j:j+kW] * kernel)
        return out

    def _forward(self, image):
        # CNN part
        maps = [self._relu(self._convolve(image, f)) for f in self.filters]
        flat = np.concatenate([m.flatten() for m in maps])

        # ANN part
        z1 = self.W1 @ flat + self.b1
        a1 = self._relu(z1)
        z2 = self.W2 @ a1 + self.b2
        a2 = self._softmax(z2)

        return a2, a1, z1, flat

    def _backward(self, a2, a1, z1, flat, label):
        # Cross entropy gradient — no one hot needed
        # dL/dz2 for cross entropy + softmax = a2 - 1 at correct class
        dz2 = a2.copy()
        dz2[label] -= 1                        # (10,)

        dW2 = np.outer(dz2, a1)
        db2 = dz2

        da1 = self.W2.T @ dz2
        dz1 = da1 * self._relu_grad(z1)

        dW1 = np.outer(dz1, flat)
        db1 = dz1

        self.W2 -= self.lr * dW2
        self.b2 -= self.lr * db2
        self.W1 -= self.lr * dW1
        self.b1 -= self.lr * db1

    # ---------- public ----------
    def train(self, X, y, epochs=3):
        self.losses = []
    
        for epoch in range(epochs):
            total_loss = 0
            correct = 0
    
            for i in range(len(X)):
                a2, a1, z1, flat = self._forward(X[i])
    
                loss = -np.log(a2[y[i]] + 1e-8)
                total_loss += loss
    
                if np.argmax(a2) == y[i]:
                    correct += 1
    
                self._backward(a2, a1, z1, flat, y[i])
    
                # Print progress every 100 images
                if (i + 1) % 100 == 0:
                    avg_loss = total_loss / (i + 1)
                    acc = correct / (i + 1) * 100
                    print(f"Epoch {epoch+1} | [{i+1}/{len(X)}] | Loss: {avg_loss:.4f} | Acc: {acc:.1f}%", end='\r')
    
            # Final line for this epoch (newline so it doesn't get overwritten)
            avg_loss = total_loss / len(X)
            acc = correct / len(X) * 100
            self.losses.append(avg_loss)
            print(f"Epoch {epoch+1} | [{len(X)}/{len(X)}] | Loss: {avg_loss:.4f} | Acc: {acc:.1f}% ✓")



    def predict(self, image):
        a2, _, _, _ = self._forward(image)
        return np.argmax(a2)

    def evaluate(self, X, y):
        correct = sum(self.predict(X[i]) == y[i] for i in range(len(X)))
        acc = correct / len(X) * 100
        print(f"Test Accuracy: {acc:.1f}%")
        return acc

    def plot_loss(self):
        plt.plot(self.losses)
        plt.title("Training Loss")
        plt.xlabel("Epoch")
        plt.ylabel("Loss")
        plt.show()