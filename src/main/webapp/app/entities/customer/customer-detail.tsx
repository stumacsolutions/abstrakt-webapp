import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './customer.reducer';
import { ICustomer } from 'app/shared/model/customer.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICustomerDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class CustomerDetail extends React.Component<ICustomerDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { customerEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="abstraktApp.customer.detail.title">Customer</Translate> [<b>{customerEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="abstraktApp.customer.name">Name</Translate>
              </span>
            </dt>
            <dd>{customerEntity.name}</dd>
            <dt>
              <span id="email">
                <Translate contentKey="abstraktApp.customer.email">Email</Translate>
              </span>
            </dt>
            <dd>{customerEntity.email}</dd>
            <dt>
              <span id="phone">
                <Translate contentKey="abstraktApp.customer.phone">Phone</Translate>
              </span>
            </dt>
            <dd>{customerEntity.phone}</dd>
            <dt>
              <span id="frequency">
                <Translate contentKey="abstraktApp.customer.frequency">Frequency</Translate>
              </span>
            </dt>
            <dd>{customerEntity.frequency}</dd>
            <dt>
              <span id="paymentAmount">
                <Translate contentKey="abstraktApp.customer.paymentAmount">Payment Amount</Translate>
              </span>
            </dt>
            <dd>{customerEntity.paymentAmount}</dd>
            <dt>
              <span id="paymentMethod">
                <Translate contentKey="abstraktApp.customer.paymentMethod">Payment Method</Translate>
              </span>
            </dt>
            <dd>{customerEntity.paymentMethod}</dd>
            <dt>
              <span id="flatPosition">
                <Translate contentKey="abstraktApp.customer.flatPosition">Flat Position</Translate>
              </span>
            </dt>
            <dd>{customerEntity.flatPosition}</dd>
            <dt>
              <span id="number">
                <Translate contentKey="abstraktApp.customer.number">Number</Translate>
              </span>
            </dt>
            <dd>{customerEntity.number}</dd>
            <dt>
              <span id="street">
                <Translate contentKey="abstraktApp.customer.street">Street</Translate>
              </span>
            </dt>
            <dd>{customerEntity.street}</dd>
            <dt>
              <Translate contentKey="abstraktApp.customer.area">Area</Translate>
            </dt>
            <dd>{customerEntity.area ? customerEntity.area.name : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/customer" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/customer/${customerEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ customer }: IRootState) => ({
  customerEntity: customer.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CustomerDetail);
