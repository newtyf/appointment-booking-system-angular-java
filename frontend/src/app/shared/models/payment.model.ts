export interface PaymentRequest {
  amount: number;
  currency: string;
  email: string;
  source_id: string;
  description?: string;
}

export interface PaymentResponse {
  id: string;
  amount: number;
  currency: string;
  email: string;
  outcome: {
    type: string;
    merchant_message: string;
    user_message: string;
  };
  created_at: string;
}

export interface CulqiToken {
  id: string;
  type: string;
  email: string;
  card_number: string;
  creation_date: number;
}

export interface CulqiCheckoutConfig {
  amount: number;
  currency: string;
  description: string;
  onSuccess: (token: CulqiToken) => void;
  onError: (error: any) => void;
}
