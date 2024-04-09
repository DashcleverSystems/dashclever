export class DialogExtension {
  private static dialogCount = 0;

  static openModal(): void {
    this.dialogCount++;
    this.setClass();
  }

  static closeModal(): void {
    this.dialogCount--;
    this.setClass();
  }

  static setClass(): void {
    if (this.dialogCount > 0) {
      document.body.classList.add('overflow-hidden');
    } else {
      document.body.classList.remove('overflow-hidden');
      this.dialogCount = 0;
    }
  }
}
