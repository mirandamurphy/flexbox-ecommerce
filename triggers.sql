CREATE FUNCTION set_updated_at() RETURNS TRIGGER
    LANGUAGE plpgsql AS
$$
    BEGIN
        NEW.updated_at := now();
        RETURN NEW;
    END;
    $$;

CREATE TRIGGER trg_address_updated_at
    BEFORE UPDATE ON address
    FOR EACH ROW
EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_cart_updated_at
    BEFORE UPDATE ON cart
    FOR EACH ROW
EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_cart_item_updated_at
    BEFORE UPDATE ON cart_item
    FOR EACH ROW
EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_checkout_session_updated_at
    BEFORE UPDATE ON checkout_session
    FOR EACH ROW
EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_invoice_updated_at
    BEFORE UPDATE ON invoice
    FOR EACH ROW
EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_marketing_consent_updated_at
    BEFORE UPDATE ON marketing_consent
    FOR EACH ROW
EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_newsletter_updated_at
    BEFORE UPDATE ON newsletter
    FOR EACH ROW
EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_order_updated_at
    BEFORE UPDATE ON "order"
    FOR EACH ROW
EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_order_item_updated_at
    BEFORE UPDATE ON order_item
    FOR EACH ROW
EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_payment_updated_at
    BEFORE UPDATE ON payment
    FOR EACH ROW
EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_payment_method_updated_at
    BEFORE UPDATE ON payment_method
    FOR EACH ROW
EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_product_updated_at
    BEFORE UPDATE ON product
    FOR EACH ROW
EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_product_inventory_updated_at
    BEFORE UPDATE ON product_inventory
    FOR EACH ROW
EXECUTE FUNCTION set_updated_at();


CREATE TRIGGER trg_subscription_box_updated_at
    BEFORE UPDATE ON subscription_box
    FOR EACH ROW
EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_subscription_plan_updated_at
    BEFORE UPDATE ON subscription_plan
    FOR EACH ROW
EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_user_updated_at
    BEFORE UPDATE ON "user"
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();


